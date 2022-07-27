package money.terra.client.rest.broadcaster

import money.terra.client.rest.api.TransactionApi
import money.terra.model.StdTx
import money.terra.model.TransactionResult
import money.terra.sdk.tools.transaction.AccountInfoProvider
import money.terra.sdk.tools.transaction.FeeEstimator
import money.terra.sdk.tools.transaction.SemaphoreProvider
import money.terra.sdk.tools.transaction.TransactionSigner
import money.terra.sdk.tools.transaction.broadcaster.BroadcastAsyncResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster

class AsyncBroadcaster(
    chainId: String,
    private val transactionApi: TransactionApi,
    signer: TransactionSigner,
    accountInfoProvider: AccountInfoProvider? = null,
    feeEstimator: FeeEstimator? = null,
    semaphore: SemaphoreProvider? = null,
) : Broadcaster<BroadcastAsyncResult>(chainId, signer, accountInfoProvider, feeEstimator, semaphore) {

    override suspend fun requestBroadcast(transaction: StdTx): BroadcastAsyncResult {
        return transactionApi.broadcastAsync(transaction).await().let {
            BroadcastAsyncResult(
                it.transactionHash,
                it.codeSpace,
                it.code,
                it.rawLog,
                it.logs,
            )
        }
    }

    override suspend fun queryTransaction(transactionHash: String): TransactionResult? {
        return transactionApi.getByHash(transactionHash).await()
    }
}