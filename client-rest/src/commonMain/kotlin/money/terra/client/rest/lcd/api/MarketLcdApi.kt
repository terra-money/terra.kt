package money.terra.client.rest.lcd.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.HttpClient
import money.terra.client.rest.api.MarketApi
import money.terra.client.rest.model.Result
import money.terra.model.Coin
import money.terra.type.Uint128

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class MarketLcdApi(
    private val client: HttpClient,
) : MarketApi {

    override fun estimateSwapResult(
        quantity: Uint128,
        offerDenomination: String,
        askDenomination: String,
    ): Deferred<Result<Coin>> = client.get(
        "/market/swap",
        mapOf("offer_coin" to quantity.toString() + offerDenomination, "ask_denom" to askDenomination),
    )
}