package money.terra.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message
import money.terra.model.PublicKey

@Serializable
data class DelegateMessage(
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("validator_address") val validatorAddress: String,
    val amount: Coin,
) : Message()

@Serializable
data class UndelegateMessage(
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("validator_address") val validatorAddress: String,
    val amount: Coin,
) : Message()

@Serializable
data class BeginRedelegateMessage(
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("validator_src_address") val sourceAddress: String,
    @SerialName("validator_dst_address") val destinationAddress: String,
    val amount: Coin,
) : Message()

@Serializable
data class ValidatorDescriptionData(
    val moniker: String,
    val identity: String,
    val website: String,
    val details: String,
)

@Serializable
data class EditValidatorMessage(
    val description: ValidatorDescriptionData,
    val address: String,
    @SerialName("commission_rate") val commissionRate: String,
    @SerialName("min_self_delegation") val minSelfDelegation: String,
) : Message()

@Serializable
data class CreateValidatorMessage(
    val description: ValidatorDescriptionData,
    val commission: CommissionRates,
    @SerialName("min_self_delegation") val minSelfDelegation: String,
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("validator_address") val validatorAddress: String,
    @SerialName("pubkey") @Contextual val publicKey: PublicKey,
    val value: Coin,
) : Message() {

    @Serializable
    data class CommissionRates(
        val rate: String,
        @SerialName("max_rate") val maxRate: String,
        @SerialName("max_change_rate") val maxChangeRate: String,
    )

//    @Serializable
//    data class Commission(
//        @SerialName("commition_rates") val commissionRates: CommissionRates,
//        @SerialName("update_time") val updateTime: LocalDateTime,
//    ) {
//    }
}