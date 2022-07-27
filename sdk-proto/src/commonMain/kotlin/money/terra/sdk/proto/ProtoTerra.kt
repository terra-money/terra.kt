package money.terra.sdk.proto

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import money.terra.client.grpc.TerraGrpcClient
import money.terra.model.TransactionResponse
import money.terra.sdk.TerraBase
import money.terra.sdk.proto.wallet.ConnectedProtoTerraWallet
import money.terra.sdk.tools.transaction.broadcaster.BroadcastResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster
import money.terra.wallet.TerraWallet

class ProtoTerra(
    chainId: String,
    broadcaster: Broadcaster,
    val client: TerraGrpcClient,
) : TerraBase(chainId, broadcaster) {

    companion object {

        fun proto(options: ProtoTerraOptions): ProtoTerra = ProtoTerra(options)

        fun proto(chainId: String, client: TerraGrpcClient): ProtoTerra = proto(ProtoTerraOptions(chainId, client))
    }

    constructor(options: ProtoTerraOptions) : this(options.chainId, options.broadcaster, options.client)

    override fun connect(wallet: TerraWallet) = ConnectedProtoTerraWallet(this, wallet)

    override fun getTransaction(transactionHash: String): Deferred<TransactionResponse?> = client.async {
        client.transactionApi.getByHash(transactionHash).await()?.transactionResponse
    }
}