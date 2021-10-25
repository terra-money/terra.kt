package money.terra.sdk.amino

import money.terra.client.rest.HttpClient
import money.terra.client.rest.TerraRestClient
import money.terra.client.rest.fcd.TerraFcdClient
import money.terra.client.rest.lcd.TerraLcdClient
import money.terra.sdk.TerraBase
import money.terra.sdk.amino.wallet.ConnectedAminoTerraWallet
import money.terra.sdk.tools.transaction.broadcaster.BroadcastResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster
import money.terra.wallet.TerraWallet

typealias Terra = AminoTerra

class AminoTerra(
    chainId: String,
    broadcaster: Broadcaster<out BroadcastResult>,
    val client: TerraRestClient,
) : TerraBase(chainId, broadcaster) {

    companion object {

        fun amino(options: AminoTerraOptions): AminoTerra = AminoTerra(options)

        fun amino(chainId: String, client: TerraRestClient): AminoTerra = amino(AminoTerraOptions(chainId, client))

        fun lcd(chainId: String, url: String): AminoTerra = amino(chainId, TerraLcdClient(chainId, url))

        fun lcd(chainId: String, client: HttpClient): AminoTerra = amino(chainId, TerraLcdClient(chainId, client))

        fun fcd(chainId: String, url: String): AminoTerra = amino(chainId, TerraFcdClient(chainId, url))

        fun fcd(chainId: String, client: HttpClient): AminoTerra = amino(chainId, TerraFcdClient(chainId, client))
    }

    constructor(options: AminoTerraOptions) : this(options.chainId, options.broadcaster, options.client)

    override fun connect(wallet: TerraWallet) = ConnectedAminoTerraWallet(this, wallet)

    override fun getTransaction(transactionHash: String) = client.transactionApi.getByHash(transactionHash)
}
