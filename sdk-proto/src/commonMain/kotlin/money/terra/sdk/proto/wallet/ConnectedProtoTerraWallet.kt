package money.terra.sdk.proto.wallet

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import money.terra.sdk.proto.ProtoTerra
import money.terra.sdk.tools.transaction.AccountInfo
import money.terra.sdk.wallet.ConnectedTerraWallet
import money.terra.wallet.TerraWallet

class ConnectedProtoTerraWallet(terra: ProtoTerra, origin: TerraWallet) : ConnectedTerraWallet(terra, origin) {

    val protoTerra = terra

    override fun getAccountInfo(): Deferred<AccountInfo?> = protoTerra.client.async {
        protoTerra.client.authApi.getAccountInfo(address).await()
    }
}