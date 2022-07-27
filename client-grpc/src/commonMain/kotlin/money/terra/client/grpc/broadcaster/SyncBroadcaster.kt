package money.terra.client.grpc.broadcaster

import money.terra.client.grpc.api.TransactionApi
import money.terra.model.Transaction
import money.terra.model.TransactionResponse
import money.terra.sdk.tools.transaction.AccountInfoProvider
import money.terra.sdk.tools.transaction.FeeEstimator
import money.terra.sdk.tools.transaction.SemaphoreProvider
import money.terra.sdk.tools.transaction.TransactionSigner
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster

class SyncBroadcaster(
    chainId: String,
    private val transactionApi: TransactionApi,
    signer: TransactionSigner,
    accountInfoProvider: AccountInfoProvider? = null,
    feeEstimator: FeeEstimator? = null,
    semaphore: SemaphoreProvider? = null,
) : Broadcaster(chainId, signer, accountInfoProvider, feeEstimator, semaphore) {

    override suspend fun requestBroadcast(transaction: Transaction): TransactionResponse {
        return transactionApi.broadcastSync(transaction).await()
    }

    override suspend fun queryTransaction(
        transactionHash: String,
    ) = transactionApi.getByHash(transactionHash).await()?.transactionResponse
}