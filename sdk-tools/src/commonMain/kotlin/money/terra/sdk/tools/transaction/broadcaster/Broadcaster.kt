package money.terra.sdk.tools.transaction.broadcaster

import kotlinx.coroutines.*
import money.terra.model.Message
import money.terra.model.Transaction
import money.terra.model.TransactionResult
import money.terra.sdk.tools.transaction.*
import money.terra.wallet.TerraWallet
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.math.min

abstract class Broadcaster<Result : BroadcastResult>(
    var chainId: String,
    var signer: TransactionSigner,
    var accountInfoProvider: AccountInfoProvider? = null,
    var feeEstimator: FeeEstimator? = null,
    var semaphore: SemaphoreProvider? = null,
) {

    abstract suspend fun requestBroadcast(transaction: Transaction): Result

    abstract suspend fun queryTransaction(transactionHash: String): TransactionResult?

    open fun broadcast(
        transaction: Transaction,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<Result> {
        if (transaction.fee == null) {
            throw BroadcastException.EmptyFee
        }

        if (!transaction.isSigned) {
            throw BroadcastException.NotSigned
        }

        return CoroutineScope(coroutineContext).async {
            requestBroadcast(transaction)
        }
    }

    open fun broadcast(
        senderWallet: TerraWallet,
        message: Message,
        memo: String = "",
        gasAmount: ULong? = null,
        feeDenomination: String? = null,
        accountNumber: ULong? = null,
        sequence: ULong? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ) = broadcast(
        senderWallet,
        Transaction(listOf(message), memo),
        gasAmount,
        feeDenomination,
        accountNumber,
        sequence,
        coroutineContext,
    )

    private suspend fun getAccountInfo(wallet: TerraWallet, accountNumber: ULong?, sequence: ULong?): AccountInfo = coroutineScope {
        val accountInfo = async(start = CoroutineStart.LAZY) {
            accountInfoProvider?.get(wallet.address) ?: throw BroadcastException.UnknownAccountInfo
        }

        val info = AccountInfo(
            wallet.address,
            accountNumber ?: accountInfo.await().accountNumber,
            null,
            sequence ?: accountInfo.await().sequence,
        )

        if (!accountInfo.isCompleted) {
            accountInfo.cancel()
        }

        info
    }

    open fun broadcast(
        senderWallet: TerraWallet,
        transaction: Transaction,
        gasAmount: ULong? = null,
        feeDenomination: String? = null,
        accountNumber: ULong? = null,
        sequence: ULong? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<Pair<Result, Transaction>> = CoroutineScope(coroutineContext).async {
        @Suppress("LocalVariableName")
        var _transaction = transaction
        var accountInfo: AccountInfo? = null

        if (_transaction.fee == null) {
            if (feeEstimator == null) {
                throw BroadcastException.EmptyFee
            }

            accountInfo = getAccountInfo(senderWallet, accountNumber, sequence)

            _transaction = try {
                if (gasAmount == null) {
                    _transaction.estimateFee(accountInfo, feeDenomination).await()
                } else {
                    _transaction.estimateFee(gasAmount, feeDenomination).await()
                }
            } catch (e: EstimateFeeException) {
                throw BroadcastException.FailedEstimateFee(e)
            }
        }

        semaphore.lockWallet<Result>(senderWallet.address) {
            if (!_transaction.isSigned) {
                if (accountInfo == null) {
                    accountInfo = getAccountInfo(senderWallet, accountNumber, sequence)
                }

                _transaction = _transaction.sign(
                    senderWallet,
                    accountInfo!!.accountNumber,
                    accountInfo!!.sequence,
                ).await()
            }

            broadcast(_transaction).await()
        } to _transaction
    }

    fun wait(
        transactionHash: String,
        intervalMillis: Long = 1000,
        initialMillis: Long = 6000,
        maxCheckCount: Int? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<TransactionResult> = CoroutineScope(coroutineContext).async {
        repeat(maxCheckCount ?: Int.MAX_VALUE) {
            val transactionResult = queryTransaction(transactionHash)

            if (transactionResult != null) {
                return@async transactionResult
            }

            delay(if (it == 0) initialMillis else intervalMillis)
        }

        throw IllegalStateException("Reach maximum check count")
    }

    protected suspend fun <T> SemaphoreProvider?.lockWallet(
        address: String,
        block: suspend () -> Result,
    ): Result {
        if (this == null) {
            return block()
        }

        val result = try {
            withPermit(address, coroutineContext = coroutineContext, block = block).await()
        } catch (e: Exception) {
            accountInfoProvider?.refreshSequence(address)
            throw e
        }

        when {
            result.isSuccess -> accountInfoProvider?.increaseSequence(address)
            result.code == 4 -> accountInfoProvider?.refreshSequence(address)
        }

        return result
    }

    @Throws(EstimateFeeException::class)
    fun Transaction.estimateFee(
        senderInfo: AccountInfo,
        feeDenomination: String? = null,
        gasAdjustment: Float? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): Deferred<Transaction> = CoroutineScope(dispatcher).async {
        val fee = feeEstimator!!.estimate(
            messages,
            senderInfo.address,
            senderInfo.accountNumber,
            senderInfo.sequence,
            feeDenomination ?: feeEstimator!!.defaultFeeDenomination,
            gasAdjustment ?: feeEstimator!!.defaultGasAdjustment,
        )

        this@estimateFee.copy(fee = fee, signatures = null)
    }

    @Throws(EstimateFeeException::class)
    fun Transaction.estimateFee(
        gasAmount: ULong,
        feeDenomination: String? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): Deferred<Transaction> = CoroutineScope(dispatcher).async {
        val fee = feeEstimator!!.estimate(gasAmount, feeDenomination ?: feeEstimator!!.defaultFeeDenomination)

        this@estimateFee.copy(fee = fee, signatures = null)
    }

    private fun Transaction.sign(
        wallet: TerraWallet,
        accountNumber: ULong,
        sequence: ULong,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): Deferred<Transaction> = CoroutineScope(dispatcher).async {
        val signatures = signatures?.toMutableList() ?: mutableListOf()

        val signData = TransactionSignData(this@sign, chainId, accountNumber, sequence)
        val signature = signer.sign(wallet, signData, this@sign)

        signatures.add(signature)

        copy(signatures = signatures)
    }
}
