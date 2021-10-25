package money.terra.amino.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
open class PolymorphicObjectSerializer<T : Any>(
    baseClass: KClass<T>,
    val classDiscriminator: String = "type",
    val valueSerialName: String = "value",
) : AbstractPolymorphicSerializer<T>(baseClass) {

    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PolymorphicObject") {
        element(classDiscriminator, String.serializer().descriptor)
        element(
            valueSerialName,
            buildSerialDescriptor("PolymorphicObject<${baseClass.simpleName}>", SerialKind.CONTEXTUAL)
        )
    }
}
