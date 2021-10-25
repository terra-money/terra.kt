package money.terra.client.rest.api

import kotlinx.coroutines.Deferred
import money.terra.client.rest.model.Result
import money.terra.type.Decimal
import money.terra.type.Uint128

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface TreasuryApi {

    fun getTaxRate(): Deferred<Result<Decimal>>

    fun getTaxCapacity(denom: String): Deferred<Result<Uint128>>
}