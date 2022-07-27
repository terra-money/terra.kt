package money.terra.sdk.tools.transaction.broadcaster

import kotlinx.coroutines.*
import money.terra.key.Key
import money.terra.model.*
import money.terra.sdk.tools.transaction.*
import money.terra.type.Binary
import money.terra.wallet.TerraWallet
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

abstract class Broadcaster(
    var chainId: String,
    var signer: TransactionSigner,
    var accountInfoProvider: AccountInfoProvider? = null,
    var feeEstimator: FeeEstimator? = null,
    var semaphore: SemaphoreProvider? = null,
) {

    abstract suspend fun requestBroadcast(transaction: Transaction): TransactionResponse

    abstract suspend fun queryTransaction(transactionHash: String): TransactionResponse?

    open fun broadcast(
        transaction: Transaction,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<TransactionResponse> {
        if (transaction.authInfo.fee.feeAmount.isEmpty()) {
            throw BroadcastException.EmptyFee
        }

        if (transaction.authInfo.signerInfos.isEmpty() || transaction.signatures.isEmpty()) {
            throw BroadcastException.NotSigned
        }

        return CoroutineScope(coroutineContext).async {
            requestBroadcast(transaction)
        }
    }

    open fun broadcast(
        senderWallet: TerraWallet,
        transaction: StdTx,
        gasAmount: Long? = null,
        feeDenomination: String? = null,
        accountNumber: Long? = null,
        sequence: Long? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<Pair<TransactionResponse, Transaction>> = CoroutineScope(coroutineContext).async {
        @Suppress("LocalVariableName")
        var _transaction = transaction.toModel(senderWallet.address)
        val transactionBody = _transaction.body
        val key = senderWallet.key ?: throw IllegalArgumentException("Wallet don't have a key")
        var accountInfo: AccountInfo? = null
        var signers: List<Signer>? = null

        if (_transaction.authInfo.fee.feeAmount.isEmpty()) {
            if (feeEstimator == null) {
                throw BroadcastException.EmptyFee
            }

            accountInfo = getAccountInfo(senderWallet, sequence)
            signers = listOf(Signer(PublicKey.Secp256k1(Binary(key.publicKey)), signer.signMode, accountInfo.sequence))

            _transaction = try {
                val fee = if (gasAmount == null) {
                    transactionBody.estimateFee(signers, feeDenomination, coroutineContext = coroutineContext).await()
                } else {
                    estimateFee(gasAmount, feeDenomination, coroutineContext).await()
                }

                _transaction.copy(authInfo = _transaction.authInfo.copy(fee = fee))
            } catch (e: EstimateFeeException) {
                throw BroadcastException.FailedEstimateFee(e)
            }
        }

        semaphore.lockWallet<TransactionResponse>(senderWallet.address) {
            if (_transaction.signatures.isEmpty()) {
                if (accountInfo == null) {
                    accountInfo = getAccountInfo(senderWallet, sequence)
                }
                if (signers == null) {
                    signers = listOf(Signer(PublicKey.Secp256k1(Binary(key.publicKey)), signer.signMode, accountInfo!!.sequence))
                }

                _transaction = transactionBody.sign(
                    key,
                    signers!!,
                    accountInfo!!,
                    _transaction.authInfo.fee,
                    coroutineContext,
                ).await()
            }

            broadcast(_transaction).await()
        } to _transaction
    }

    open fun broadcast(
        senderWallet: TerraWallet,
        message: Message,
        memo: String = "",
        gasAmount: Long? = null,
        feeDenomination: String? = null,
        accountNumber: Long? = null,
        sequence: Long? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ) = broadcast(
        senderWallet,
        StdTx(listOf(message), memo),
        gasAmount,
        feeDenomination,
        accountNumber,
        sequence,
        coroutineContext,
    )

    fun wait(
        transactionHash: String,
        intervalMillis: Long = 1000,
        initialMillis: Long = 6000,
        maxCheckCount: Int? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<TransactionResponse> = CoroutineScope(coroutineContext).async {
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
        block: suspend () -> TransactionResponse,
    ): TransactionResponse {
        if (this == null) {
            return block()
        }

        val result = try {
            withPermit(address, coroutineContext = coroutineContext, block = block).await()
        } catch (e: Exception) {
            accountInfoProvider?.refreshSequence(address)
            throw e
        }

        when (result.code) {
            0 -> accountInfoProvider?.increaseSequence(address)
            4 -> accountInfoProvider?.refreshSequence(address)
        }

        return result
    }

    @Throws(EstimateFeeException::class)
    fun TransactionBody.estimateFee(
        signers: List<Signer>,
        feeDenomination: String? = null,
        gasAdjustment: Float? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<Fee> = CoroutineScope(coroutineContext).async {
        feeEstimator?.estimate(
            this@estimateFee,
            signers,
            feeDenomination ?: feeEstimator!!.defaultFeeDenomination,
            gasAdjustment ?: feeEstimator!!.defaultGasAdjustment,
        ) ?: throw IllegalStateException("Not set FeeEstimator")
    }

    @Throws(EstimateFeeException::class)
    fun estimateFee(
        gasAmount: Long,
        feeDenomination: String? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): Deferred<Fee> = CoroutineScope(coroutineContext).async {
        feeEstimator?.estimate(gasAmount, feeDenomination ?: feeEstimator!!.defaultFeeDenomination)
            ?: throw IllegalStateException("Not set FeeEstimator")
    }

    private fun TransactionBody.sign(
        key: Key,
        signers: List<Signer>,
        accountInfo: AccountInfo,
        fee: Fee,
        dispatcher: CoroutineContext = Dispatchers.Default,
    ): Deferred<Transaction> = CoroutineScope(dispatcher).async {
        val signatures = mutableListOf<Binary>()
        val authInfo = AuthInfo(signers, fee)
        val signData = TransactionSignDocument(this@sign, authInfo, chainId, accountInfo.accountNumber)
        val signature = signer.sign(key, signData)

        signatures.add(Binary(signature))

        Transaction(this@sign, authInfo, signatures)
    }

    private suspend fun getAccountInfo(
        wallet: TerraWallet,
        sequence: Long? = null,
    ): AccountInfo {
        val accountInfo = accountInfoProvider?.get(wallet.address) ?: throw BroadcastException.UnknownAccountInfo

        return sequence?.let { accountInfo.copy(sequence = it) } ?: accountInfo
    }
}
