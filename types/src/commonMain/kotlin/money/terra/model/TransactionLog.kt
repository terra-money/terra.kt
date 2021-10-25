package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.TransactionEvent

@Serializable
data class TransactionLog(
    val events: List<TransactionEvent>,
    @SerialName("msg_index") val index: UInt? = null,
    val log: String? = null,
)
