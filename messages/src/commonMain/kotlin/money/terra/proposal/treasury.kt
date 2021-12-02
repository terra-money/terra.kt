package money.terra.proposal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.message.Proposal

@Serializable
data class TaxRateUpdateProposal(
    val title: String,
    val description: String,
    @SerialName("tax_rate") val taxRate: String,
) : Proposal()

@Serializable
data class RewardWeightUpdateProposal(
    val title: String,
    val description: String,
    @SerialName("reward_weight") val rewardWeight: String,
) : Proposal()