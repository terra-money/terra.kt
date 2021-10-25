package money.terra.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message

@Serializable
data class SwapMessage(
    val trader: String,
    @SerialName("offer_coin") val offerCoin: Coin,
    @SerialName("ask_denom") val askDenomination: String,
) : Message()

@Serializable
data class SwapSendMessage(
    @SerialName("from_address") val fromAddress: String,
    @SerialName("to_address") val toAddress: String,
    @SerialName("offer_coin") val offerCoin: Coin,
    @SerialName("ask_denom") val askDenomination: String,
) : Message()

