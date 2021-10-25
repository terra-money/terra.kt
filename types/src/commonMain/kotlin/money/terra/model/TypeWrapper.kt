package money.terra.model

import kotlinx.serialization.Serializable

@Serializable
data class TypeWrapper<T>(
    val type: String,
    val value: T,
)
