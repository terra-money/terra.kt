package money.terra.client.rest.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.model.Result
import money.terra.model.Coin
import money.terra.type.Uint128

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface MarketApi {

    fun estimateSwapResult(
        quantity: Uint128,
        offerDenomination: String,
        askDenomination: String,
    ): Deferred<Result<Coin>>
}