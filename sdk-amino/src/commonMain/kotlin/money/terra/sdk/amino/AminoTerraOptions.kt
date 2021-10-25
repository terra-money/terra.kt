package money.terra.sdk.amino

import money.terra.client.rest.TerraRestClient
import money.terra.client.rest.broadcaster.AsyncBroadcaster
import money.terra.client.rest.broadcaster.BlockBroadcaster
import money.terra.client.rest.broadcaster.SyncBroadcaster
import money.terra.client.rest.fcd.TerraFcdClient
import money.terra.client.rest.provider.AlwaysFetchAccountInfoProvider
import money.terra.client.rest.provider.GasPricesFcdProvider
import money.terra.client.rest.provider.NodeFeeEstimator
import money.terra.sdk.amino.wallet.AminoTransactionSigner
import money.terra.sdk.tools.transaction.*
import money.terra.sdk.tools.transaction.broadcaster.BroadcastResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster
import money.terra.type.Decimal

data class AminoTerraOptions(
    val chainId: String,
    val client: TerraRestClient,
    val accountInfoProvider: AccountInfoProvider? = AlwaysFetchAccountInfoProvider(client),
    val gasPricesProvider: GasPricesProvider? = if (client is TerraFcdClient) {
        GasPricesFcdProvider(client)
    } else {
        StaticGasPricesProvider(mapOf("uluna" to Decimal("0.01")))
    },
    val feeEstimator: FeeEstimator? = gasPricesProvider?.let { NodeFeeEstimator(client.transactionApi, it) },
    val semaphoreProvider: SemaphoreProvider? = null,
    val broadcaster: Broadcaster<out BroadcastResult> = SyncBroadcaster(
        chainId,
        client.transactionApi,
        AminoTransactionSigner,
        accountInfoProvider,
        feeEstimator,
        semaphoreProvider,
    )
) {

    fun asyncBroadcaster() = copy(
        broadcaster = AsyncBroadcaster(
            chainId,
            client.transactionApi,
            AminoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )

    fun syncBroadcaster() = copy(
        broadcaster = SyncBroadcaster(
            chainId,
            client.transactionApi,
            AminoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )

    fun blockBroadcaster() = copy(
        broadcaster = BlockBroadcaster(
            chainId,
            client.transactionApi,
            AminoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )
}