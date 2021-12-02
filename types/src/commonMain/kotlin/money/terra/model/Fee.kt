package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer

@Serializable
data class Fee(
    @SerialName("gas") @Serializable(LongAsStringSerializer::class) val gasAmount: Long,
    @SerialName("amount") val feeAmount: List<Coin>,
)
