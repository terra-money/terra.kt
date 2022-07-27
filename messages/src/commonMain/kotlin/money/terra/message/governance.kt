package money.terra.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message

abstract class Proposal

@Serializable
data class SubmitProposalMessage(
    @Contextual val content: Proposal,
    @SerialName("initial_deposit") val initialDeposit: List<Coin>,
    val proposer: String,
) : Message()

@Serializable
data class DepositProposalMessage(
    @SerialName("proposal_id") val proposalId: Long,
    val depositor: String,
    val amount: List<Coin>,
) : Message()

@Serializable
data class VoteProposalMessage(
    @SerialName("proposal_id") val proposalId: Long,
    val voter: String,
    val option: VoteOption,
) : Message() {

    @Serializable
    enum class VoteOption {
        YES,
        ABSTAIN,
        NO,
        NO_WITH_VETO,
        UNSPECIFIED,
    }
}
