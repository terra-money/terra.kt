package money.terra.sdk.tools.transaction

import money.terra.model.CoinDecimal
import money.terra.type.Decimal

interface GasPricesProvider {

    suspend fun get(denomination: String): CoinDecimal
}

class StaticGasPricesProvider(val gasPrices: Map<String, Decimal>) : GasPricesProvider {

    override suspend fun get(denomination: String): CoinDecimal {
        val gasPrice = gasPrices[denomination]
            ?: throw IllegalStateException("Unknown gasPrice for $denomination")

        return CoinDecimal(denomination, gasPrice)
    }
}