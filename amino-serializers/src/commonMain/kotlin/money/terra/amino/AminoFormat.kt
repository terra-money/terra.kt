package money.terra.amino

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import kotlinx.serialization.modules.plus
import money.terra.amino.serializer.message.MessageSerializersModule
import money.terra.amino.serializer.type.TypeSerializersModule

@OptIn(ExperimentalSerializationApi::class)
object AminoFormat : StringFormat {

    override val serializersModule: SerializersModule
        get() = _serializersModule

    @Suppress("ObjectPropertyName")
    private var _serializersModule: SerializersModule = TypeSerializersModule + MessageSerializersModule

    private var json = createJsonFormat()

    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String = json.encodeToString(serializer, value)

    fun <T> encodeToJsonElement(
        serializer: SerializationStrategy<T>,
        value: T,
    ): JsonElement = json.encodeToJsonElement(serializer, value)

    override fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String,
    ): T = json.decodeFromString(deserializer, string)

    fun <T> decodeFromJsonElement(
        deserializer: DeserializationStrategy<T>,
        element: JsonElement,
    ): T = json.decodeFromJsonElement(deserializer, element)

    fun registerSerializersModule(module: SerializersModule, overwrite: Boolean = false) {
        _serializersModule = if (overwrite) {
            serializersModule.overwriteWith(module)
        } else {
            serializersModule + module
        }

        json = createJsonFormat()
    }

    private fun createJsonFormat() = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true

        serializersModule = this@AminoFormat.serializersModule
    }
}
