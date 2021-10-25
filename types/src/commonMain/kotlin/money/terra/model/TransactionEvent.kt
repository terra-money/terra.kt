package money.terra.model

import kotlinx.serialization.Serializable
import money.terra.model.Attribute

@Serializable
data class TransactionEvent(
    val type: String,
    val attributes: List<Attribute>,
)
