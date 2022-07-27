package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeWrapper<T>(
    @SerialName("type_url") val type: String,
    val value: T,
)
