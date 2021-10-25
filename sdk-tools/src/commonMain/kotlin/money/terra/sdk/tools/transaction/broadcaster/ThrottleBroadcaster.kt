package money.terra.sdk.tools.transaction.broadcaster

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import kr.jadekim.common.extension.sequentialGroupBy
import money.terra.model.Message
import money.terra.model.Transaction
import money.terra.wallet.TerraWallet
import kotlin.coroutines.CoroutineContext

class ThrottleBroadcaster<Result : BroadcastResult>(
    private val delegate: Broadcaster<Result>,
    val queue: BroadcastQueue<Result> = LocalBroadcastQueue(),
    @Suppress("CanBeParameter") val broadcastLockReleaser: BroadcastLockReleaser = StaticIntervalBroadcastLockReleaser(6),
    var messageWaitMillis: Long = 0,
    val maxTransactionPerBlock: Int = Int.MAX_VALUE,
    val maxMessagePerTransaction: Int = Int.MAX_VALUE,
    override val coroutineContext: CoroutineContext = Dispatchers.Default,
) : Broadcaster<Result>(
    delegate.chainId,
    delegate.signer,
    delegate.accountInfoProvider,
    delegate.feeEstimator,
    delegate.semaphore,
), CoroutineScope {

    private val broadcastSemaphore = Semaphore(maxTransactionPerBlock)

    private val supervisor = SupervisorJob()
    private var broadcastJob: Job? = null
    private var messageGroupingJob: Job? = null

    init {
        broadcastLockReleaser.registerListener {
            releaseBroadcastLock()
        }
    }

    fun startup() {
        if (broadcastJob?.isActive != true) {
            launchBroadcastJob()
        }
    }

    fun shutdown(): Deferred<*> = async {
        broadcastJob?.cancelAndJoin()
    }

    override suspend fun requestBroadcast(transaction: Transaction) = delegate.requestBroadcast(transaction)

    override suspend fun queryTransaction(transactionHash: String) = delegate.queryTransaction(transactionHash)

    //이미 서명된 트랜잭션을 지연시킬 경우 시퀀스 오류가 발생할 가능성이 있기 때문에 지연 대상에서 제외
    override fun broadcast(transaction: Transaction, coroutineContext: CoroutineContext): Deferred<Result> {
        return delegate.broadcast(transaction, coroutineContext)
    }

    override fun broadcast(
        senderWallet: TerraWallet,
        message: Message,
        memo: String,
        gasAmount: ULong?,
        feeDenomination: String?,
        accountNumber: ULong?,
        sequence: ULong?,
        coroutineContext: CoroutineContext,
    ): Deferred<Pair<Result, Transaction>> {
        val result = CompletableDeferred<Pair<Result, Transaction>>()
        val queueItem = MessageQueueItem(
            senderWallet,
            message,
            memo,
            gasAmount,
            feeDenomination,
            result,
        )

        launch {
            queue.pushMessage(queueItem)
        }.invokeOnCompletion {
            if (it != null) {
                result.completeExceptionally(it)
            }
        }

        return result
    }

    override fun broadcast(
        senderWallet: TerraWallet,
        transaction: Transaction,
        gasAmount: ULong?,
        feeDenomination: String?,
        accountNumber: ULong?,
        sequence: ULong?,
        coroutineContext: CoroutineContext,
    ): Deferred<Pair<Result, Transaction>> {
        val result = CompletableDeferred<Pair<Result, Transaction>>()
        val queueItem = TransactionQueueItem(
            senderWallet,
            transaction.copy(signatures = null),
            gasAmount,
            feeDenomination,
            result,
        )

        launch {
            queue.pushTransaction(queueItem)
        }.invokeOnCompletion {
            if (it != null) {
                result.completeExceptionally(it)
            }
        }

        return result
    }

    private suspend fun broadcast(
        wallet: TerraWallet,
        memo: String,
        items: List<MessageQueueItem<Result>>
    ): Pair<Result, Transaction> {
        val transaction = Transaction.builder()
            .memo(memo)
            .message(items.map { it.message })
            .build()

        return broadcast(wallet, transaction).await()
    }

    private suspend fun broadcast(item: TransactionQueueItem<Result>): Pair<Result, Transaction> {
        return broadcast(item.wallet, item.transaction).await()
    }

    private fun launchBroadcastJob() {
        broadcastJob = launch(supervisor) {
            while (isActive) {
                broadcastSemaphore.acquire()

                val item = queue.popTransaction()

                withContext(NonCancellable) {
                    try {
                        item.result.complete(broadcast(item))
                    } catch (e: Exception) {
                        item.result.completeExceptionally(e)
                    }
                }
            }
        }
    }

    private fun launchMessageGroupingJob() {
        messageGroupingJob = launch(supervisor) {
            while (isActive) {
                val items = popMessages()

                withContext(NonCancellable) {
                    for ((walletAndMemo, messages) in items.sequentialGroupBy { it.wallet to it.memo }) {
                        try {
                            val result = broadcast(walletAndMemo.first, walletAndMemo.second, messages)

                            messages.forEach { it.result.complete(result) }
                        } catch (e: Exception) {
                            messages.forEach { it.result.completeExceptionally(e) }
                        }
                    }
                }
            }
        }
    }

    private suspend fun popMessages(): List<MessageQueueItem<Result>> {
        val items = mutableListOf<MessageQueueItem<Result>>()

        try {
            repeat(maxMessagePerTransaction) {
                val item = popMessage(it == 0) ?: return items

                items.add(item)
            }
        } catch (e: CancellationException) {
            withContext(NonCancellable) {
                items.forEach { queue.pushMessage(it) }
            }
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            withContext(NonCancellable) {
                items.forEach { queue.pushMessage(it) }
            }
        }

        return items
    }

    private suspend fun popMessage(isFirst: Boolean = false): MessageQueueItem<Result>? {
        if (isFirst) {
            return queue.popMessage()
        }

        if (messageWaitMillis > 0) {
            return withTimeoutOrNull(messageWaitMillis) {
                queue.popMessage()
            }
        }

        if (queue.isEmptyMessageQueue()) {
            return null
        }

        return queue.popMessage()
    }

    private fun releaseBroadcastLock() {
        repeat(maxTransactionPerBlock - broadcastSemaphore.availablePermits) {
            broadcastSemaphore.release()
        }
    }
}

interface BroadcastQueue<Result : BroadcastResult> {

    suspend fun popTransaction(): TransactionQueueItem<Result>

    suspend fun pushTransaction(item: TransactionQueueItem<Result>)

    suspend fun popMessage(): MessageQueueItem<Result>

    suspend fun pushMessage(item: MessageQueueItem<Result>)

    suspend fun isEmptyMessageQueue(): Boolean
}

class LocalBroadcastQueue<Result : BroadcastResult>(
    private val transactionQueue: Channel<TransactionQueueItem<Result>> = Channel(),
    private val messageQueue: Channel<MessageQueueItem<Result>> = Channel(),
) : BroadcastQueue<Result> {

    override suspend fun popTransaction(): TransactionQueueItem<Result> = transactionQueue.receive()

    override suspend fun pushTransaction(item: TransactionQueueItem<Result>) = transactionQueue.send(item)

    override suspend fun popMessage(): MessageQueueItem<Result> = messageQueue.receive()

    override suspend fun pushMessage(item: MessageQueueItem<Result>) = messageQueue.send(item)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun isEmptyMessageQueue(): Boolean = messageQueue.isEmpty
}

data class TransactionQueueItem<Result : BroadcastResult>(
    val wallet: TerraWallet,
    val transaction: Transaction,
    val gasAmount: ULong?,
    val feeDenomination: String?,
    val result: CompletableDeferred<Pair<Result, Transaction>>,
)

data class MessageQueueItem<Result : BroadcastResult>(
    val wallet: TerraWallet,
    val message: Message,
    val memo: String,
    val gasAmount: ULong?,
    val feeDenomination: String?,
    val result: CompletableDeferred<Pair<Result, Transaction>>,
)

interface BroadcastLockReleaser {

    fun start()

    fun registerListener(listener: () -> Unit)

    fun stop()
}

class StaticIntervalBroadcastLockReleaser(val intervalMillis: Long) : BroadcastLockReleaser {

    private val listeners: MutableList<() -> Unit> = mutableListOf()

    private var job: Job? = null

    override fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            listeners.forEach { it.invoke() }
        }
    }

    override fun registerListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    override fun stop() {
        job?.cancel()
    }
}
