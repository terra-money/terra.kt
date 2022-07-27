package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.type.Decimal
import money.terra.type.toUint128

@Serializable
data class Fee(
    @SerialName("gas") @Serializable(LongAsStringSerializer::class) val gasLimit: Long,
    @SerialName("amount") val feeAmount: List<Coin>,
    val payer: String? = null,
    val granter: String? = null,
) {

    companion object {
        val EMPTY = Fee(0, emptyList())
    }

    constructor(gasAmount: Long, gasPrice: CoinDecimal, payer: String? = null, granter: String? = null) : this(
        gasAmount,
        listOf(Coin(gasPrice.denomination, gasPrice.amount.times(Decimal(gasAmount.toString())).toUint128())),
        payer,
        granter,
    )
}
