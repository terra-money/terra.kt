package money.terra.amino.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

open class EmptyToNullSerializer<T : Any>(val origin: KSerializer<T>) : KSerializer<T?> {

    override val descriptor: SerialDescriptor = origin.descriptor.nullable

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun serialize(encoder: Encoder, value: T?) {
        encoder.encodeNullableSerializableValue(this, value)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun deserialize(decoder: Decoder): T? {
        if (!decoder.decodeNotNullMark()) {
            return decoder.decodeNull()
        }

        val jsonDecoder = decoder as JsonDecoder

        return when (val data = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> jsonDecoder.json.decodeFromJsonElement(origin, data)
            JsonNull -> null
            is JsonObject -> if (data.isEmpty()) null else {
                jsonDecoder.json.decodeFromJsonElement(origin, data)
            }
            is JsonArray -> if (data.isEmpty()) null else {
                jsonDecoder.json.decodeFromJsonElement(origin, data)
            }
        }
    }
}