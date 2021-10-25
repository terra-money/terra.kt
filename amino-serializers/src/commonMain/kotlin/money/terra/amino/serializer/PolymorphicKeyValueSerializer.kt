package money.terra.amino.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
open class PolymorphicKeyValueSerializer<T : Any>(
    baseClass: KClass<T>,
) : AbstractPolymorphicSerializer<T>(baseClass) {

    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor(
        "PolymorphicKeyValue",
        StructureKind.MAP,
        String.serializer().descriptor,
        JsonElement.serializer().descriptor,
    ) {
        element("0", String.serializer().descriptor)
        element("1", buildSerialDescriptor("PolymorphicKeyValue<${baseClass.simpleName}>", SerialKind.CONTEXTUAL))
    }
}