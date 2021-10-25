package money.terra.message

import kotlinx.serialization.Serializable
import money.terra.model.Message

@Serializable
data class UnjailMessage(
    val address: String,
) : Message()