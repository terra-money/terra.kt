package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import money.terra.type.Binary

@Serializable
data class StringAttribute(
    val key: String,
    val value: String,
)

@Serializable
data class BinaryAttribute(
    @Contextual val key: Binary,
    @Contextual val value: Binary,
)
