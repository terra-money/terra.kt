package money.terra.client.rest.lcd.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.api.TreasuryApi
import money.terra.client.rest.model.Result
import money.terra.client.rest.HttpClient
import money.terra.type.Decimal
import money.terra.type.Uint128

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class TreasuryLcdApi(
    private val client: HttpClient,
) : TreasuryApi {

    override fun getTaxRate(): Deferred<Result<Decimal>> {
        return client.get("/treasury/tax_rate")
    }

    override fun getTaxCapacity(denom: String): Deferred<Result<Uint128>> {
        return client.get("/treasury/tax_cap/$denom")
    }
}