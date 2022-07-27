package money.terra.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionStringEvent(
    val type: String,
    val attributes: List<StringAttribute>,
)

@Serializable
data class TransactionBinaryEvent(
    val type: String,
    val attributes: List<BinaryAttribute>,
)
