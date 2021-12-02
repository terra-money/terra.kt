package money.terra.proposal

import kotlinx.serialization.Serializable
import money.terra.message.Proposal

@Serializable
data class ParameterChangeProposal(
    val title: String,
    val description: String,
    val changes: List<Change>,
) : Proposal() {

    @Serializable
    data class Change(
        val subspace: String,
        val key: String,
        val subkey: String,
        val value: String,
    )
}