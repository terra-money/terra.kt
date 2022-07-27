package money.terra.client.rest.fcd

import kotlinx.coroutines.Deferred
import money.terra.client.rest.DEFAULT_TIMEOUT_MILLIS
import money.terra.client.rest.HttpClient
import money.terra.client.rest.TerraRestClient
import money.terra.client.rest.lcd.TerraLcdClient
import money.terra.type.Decimal

@Deprecated("This client is developed for v1. Use grpc client", level = DeprecationLevel.ERROR)
class TerraFcdClient(
    val chainId: String,
    val httpClient: HttpClient,
    val lcdClient: TerraLcdClient = TerraLcdClient(chainId, httpClient)
) : TerraRestClient by lcdClient {

    constructor(
        chainId: String,
        serverUrl: String,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    ) : this(chainId, HttpClient(serverUrl, timeoutMillis))

    constructor(
        chainId: String,
        serverUrl: String,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
        lcdClient: TerraLcdClient = TerraLcdClient(chainId, serverUrl, timeoutMillis),
    ) : this(chainId, HttpClient(serverUrl, timeoutMillis), lcdClient)

    fun getGasPrices(): Deferred<Map<String, Decimal>> =
        httpClient.get("/v1/txs/gas_prices")
}