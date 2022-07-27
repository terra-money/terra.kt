package money.terra.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Message

@Serializable
data class UnjailMessage(
    @SerialName("validator_addr") val address: String,
) : Message()