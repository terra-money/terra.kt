package money.terra.sdk.proto

import money.terra.client.grpc.TerraGrpcClient
import money.terra.client.grpc.broadcaster.AsyncBroadcaster
import money.terra.client.grpc.broadcaster.BlockBroadcaster
import money.terra.client.grpc.broadcaster.SyncBroadcaster
import money.terra.client.grpc.provider.AlwaysFetchAccountInfoProvider
import money.terra.client.grpc.provider.NodeFeeEstimator
import money.terra.sdk.proto.wallet.ProtoTransactionSigner
import money.terra.sdk.tools.transaction.*
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster
import money.terra.type.Decimal

data class ProtoTerraOptions(
    val chainId: String,
    val client: TerraGrpcClient,
    val accountInfoProvider: AccountInfoProvider? = AlwaysFetchAccountInfoProvider(client),
    val gasPricesProvider: GasPricesProvider? = StaticGasPricesProvider(mapOf("uluna" to Decimal("0.15"))),
    val feeEstimator: FeeEstimator? = gasPricesProvider?.let { NodeFeeEstimator(client.transactionApi, it) },
    val semaphoreProvider: SemaphoreProvider? = null,
    val broadcaster: Broadcaster = SyncBroadcaster(
        chainId,
        client.transactionApi,
        ProtoTransactionSigner,
        accountInfoProvider,
        feeEstimator,
        semaphoreProvider,
    )
) {

    fun asyncBroadcaster() = copy(
        broadcaster = AsyncBroadcaster(
            chainId,
            client.transactionApi,
            ProtoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )

    fun syncBroadcaster() = copy(
        broadcaster = SyncBroadcaster(
            chainId,
            client.transactionApi,
            ProtoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )

    fun blockBroadcaster() = copy(
        broadcaster = BlockBroadcaster(
            chainId,
            client.transactionApi,
            ProtoTransactionSigner,
            accountInfoProvider,
            feeEstimator,
            semaphoreProvider,
        )
    )
}