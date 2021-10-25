package money.terra.amino.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor

open class AminoSerializer<T>(
    private val origin: KSerializer<T>,
    serialName: String? = null,
) : KSerializer<T> by origin {

    override val descriptor: SerialDescriptor = if (serialName == null) origin.descriptor else {
        RenamedSerialDescriptor(origin.descriptor, serialName)
    }
}

@OptIn(ExperimentalSerializationApi::class)
private class RenamedSerialDescriptor(
    private val origin: SerialDescriptor,
    override val serialName: String,
) : SerialDescriptor by origin