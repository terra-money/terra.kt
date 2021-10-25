package money.terra.client.rest.broadcaster

import money.terra.client.rest.api.TransactionApi
import money.terra.model.Transaction
import money.terra.model.TransactionResult
import money.terra.sdk.tools.transaction.AccountInfoProvider
import money.terra.sdk.tools.transaction.FeeEstimator
import money.terra.sdk.tools.transaction.SemaphoreProvider
import money.terra.sdk.tools.transaction.TransactionSigner
import money.terra.sdk.tools.transaction.broadcaster.BroadcastBlockResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster

class BlockBroadcaster(
    chainId: String,
    private val transactionApi: TransactionApi,
    signer: TransactionSigner,
    accountInfoProvider: AccountInfoProvider? = null,
    feeEstimator: FeeEstimator? = null,
    semaphore: SemaphoreProvider? = null,
) : Broadcaster<BroadcastBlockResult>(chainId, signer, accountInfoProvider, feeEstimator, semaphore) {

    override suspend fun requestBroadcast(transaction: Transaction): BroadcastBlockResult {
        return transactionApi.broadcastBlock(transaction).await().let {
            BroadcastBlockResult(
                it.height,
                it.transactionHash,
                it.codeSpace,
                it.code,
                it.rawLog,
                it.logs,
                it.gasUsed,
                it.gasWanted,
            )
        }
    }

    override suspend fun queryTransaction(transactionHash: String): TransactionResult? {
        return transactionApi.getByHash(transactionHash).await()
    }
}