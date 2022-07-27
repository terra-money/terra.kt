package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionLog(
    @SerialName("msg_index") val index: Int,
    val log: String,
    val events: List<TransactionStringEvent>,
)
