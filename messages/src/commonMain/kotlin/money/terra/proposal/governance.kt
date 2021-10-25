package money.terra.proposal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import money.terra.message.Proposal

val GovernanceProposalSerializerModule = SerializersModule {
    polymorphic(Proposal::class) {
        subclass(ParameterChangeProposal::class)
    }
}

@Serializable
@SerialName("gov/ParameterChangeProposal")
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