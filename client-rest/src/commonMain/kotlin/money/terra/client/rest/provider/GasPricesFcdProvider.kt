package money.terra.client.rest.provider

import kr.jadekim.common.util.currentTimeMillis
import money.terra.client.rest.fcd.TerraFcdClient
import money.terra.sdk.tools.transaction.GasPricesProvider
import money.terra.type.Decimal

class GasPricesFcdProvider(
    private val fcdClient: TerraFcdClient,
) : GasPricesProvider {

    var cacheLifeTimeMillis: Long = 60 * 1000 //a minute

    private var lastPrices: Map<String, Decimal>? = null
    private var lastUpdateTime: Long = 0

    override suspend fun get(denomination: String): Decimal {
        var gasPrices = lastPrices

        if (lastUpdateTime + cacheLifeTimeMillis < currentTimeMillis() || gasPrices == null) {
            gasPrices = fcdClient.getGasPrices().await()
            lastPrices = gasPrices
        }

        return gasPrices[denomination]
            ?: throw IllegalStateException("Unknown gasPrice for $denomination")
    }
}