package money.terra.sdk.tools.transaction

import money.terra.type.Decimal

interface GasPricesProvider {

    suspend fun get(denomination: String): Decimal
}

class StaticGasPricesProvider(val gasPrices: Map<String, Decimal>) : GasPricesProvider {

    override suspend fun get(denomination: String): Decimal = gasPrices[denomination]
        ?: throw IllegalStateException("Unknown gasPrice for $denomination")
}