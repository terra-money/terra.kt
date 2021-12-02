package money.terra.client.rest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.model.Coin
import money.terra.model.CoinDecimal

@Serializable
data class BaseRequest(
    @SerialName("from") val fromAddress: String,
    val memo: String,
    @SerialName("chain_id") val chainId: String,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long,
    @Serializable(LongAsStringSerializer::class) val sequence: Long,
    @SerialName("timeout_height") @Serializable(LongAsStringSerializer::class) val timeoutHeight: Long? = null,
    val fees: List<Coin>? = null,
    @SerialName("gas_prices") val gasPrices: List<CoinDecimal>? = null,
    val gas: String? = null,
    @SerialName("gas_adjustment") val gasAdjustment: String? = null,
    val simulate: Boolean = false,
)
