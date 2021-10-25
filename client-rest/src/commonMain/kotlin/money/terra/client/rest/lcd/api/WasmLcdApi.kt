package money.terra.client.rest.lcd.api

import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.JsonElement
import money.terra.client.rest.api.WasmApi
import money.terra.client.rest.model.Result
import money.terra.client.rest.HttpClient
import money.terra.client.rest.asUrlEncoded

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class WasmLcdApi(
    val client: HttpClient,
) : WasmApi {

    override fun <T : Any> query(address: String, message: String): Deferred<Result<JsonElement>> {
        return client.get("/wasm/contracts/$address/store", mapOf("query_msg" to message.asUrlEncoded()))
    }
}