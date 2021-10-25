package money.terra.sdk.amino.wallet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import money.terra.sdk.amino.AminoTerra
import money.terra.sdk.tools.transaction.AccountInfo
import money.terra.sdk.wallet.ConnectedTerraWallet
import money.terra.wallet.TerraWallet

class ConnectedAminoTerraWallet(terra: AminoTerra, origin: TerraWallet) : ConnectedTerraWallet(terra, origin) {

    val aminoTerra = terra

    override fun getAccountInfo(): Deferred<AccountInfo> = CoroutineScope(Dispatchers.Default).async {
        aminoTerra.client.authApi.getAccountInfo(address).await().result.value?.let {
            AccountInfo(
                it.address,
                it.accountNumber,
                it.publicKey,
                it.sequence,
            )
        } ?: AccountInfo(address)
    }
}