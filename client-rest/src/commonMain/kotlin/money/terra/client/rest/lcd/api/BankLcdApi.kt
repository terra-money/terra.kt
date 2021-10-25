package money.terra.client.rest.lcd.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.api.BankApi
import money.terra.client.rest.model.Result
import money.terra.client.rest.HttpClient
import money.terra.model.Coin

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class BankLcdApi(
    private val client: HttpClient,
) : BankApi {

    override fun getAccountBalances(address: String): Deferred<Result<List<Coin>>> {
        return client.get("/bank/balances/$address")
    }
}