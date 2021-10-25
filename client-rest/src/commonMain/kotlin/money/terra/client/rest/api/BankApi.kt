package money.terra.client.rest.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.model.Result
import money.terra.model.Coin

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface BankApi {

    fun getAccountBalances(address: String): Deferred<Result<List<Coin>>>
}