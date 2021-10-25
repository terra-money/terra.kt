package money.terra.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message

@Serializable
data class SendMessage(
    @SerialName("from_address") val fromAddress: String,
    @SerialName("to_address") val toAddress: String,
    val amount: List<Coin>,
) : Message()

@Serializable
data class MultipleSendMessage(
    val inputs: List<Amount>,
    val outputs: List<Amount>,
) : Message(){

    @Serializable
    data class Amount(
        val address: String,
        val coins: List<Coin>,
    )
}