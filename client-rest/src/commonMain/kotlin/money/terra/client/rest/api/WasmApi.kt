package money.terra.client.rest.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import money.terra.amino.AminoFormat
import money.terra.client.rest.model.Result
import money.terra.model.EnumMessage

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface WasmApi {

    fun <T : Any> query(address: String, message: String): Deferred<Result<JsonElement>>
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
inline fun <reified T : Any> WasmApi.query(address: String, message: EnumMessage): Deferred<Result<T>> {
    val serialized = AminoFormat.encodeToString(message)
    val response = query<Deferred<Result<JsonElement>>>(address, serialized)

    return CoroutineScope(Dispatchers.Unconfined).async {
        val result = response.await()
        val data = AminoFormat.decodeFromJsonElement<T>(AminoFormat.serializersModule.serializer(), result.result)

        Result(result.height, data)
    }
}