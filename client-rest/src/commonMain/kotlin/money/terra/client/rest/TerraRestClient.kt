package money.terra.client.rest

import money.terra.client.rest.api.*

interface TerraRestClient {

    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val authApi: AuthApi
    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val bankApi: BankApi
    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val marketApi: MarketApi
    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val transactionApi: TransactionApi
    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val treasuryApi: TreasuryApi
    @Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
    val wasmApi: WasmApi
}