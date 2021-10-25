package money.terra.model

import kotlinx.serialization.Serializable

@Serializable
data class Attribute(
    val key: String,
    val value: String,
)
