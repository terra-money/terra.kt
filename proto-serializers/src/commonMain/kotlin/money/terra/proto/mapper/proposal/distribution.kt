package money.terra.proto.mapper.proposal

import cosmos.distribution.v1beta1.communityPoolSpendProposal
import money.terra.proposal.CommunityPoolSpendProposal
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object CommunityPoolSpendProposalMapper :
    ProtobufTypeMapper<CommunityPoolSpendProposal, cosmos.distribution.v1beta1.Distribution.CommunityPoolSpendProposal> {

    override val descriptor = cosmos.distribution.v1beta1.Distribution.CommunityPoolSpendProposal.getDescriptor()

    override val parser = cosmos.distribution.v1beta1.Distribution.CommunityPoolSpendProposal.parser()

    override fun convert(obj: cosmos.distribution.v1beta1.Distribution.CommunityPoolSpendProposal) =
        CommunityPoolSpendProposal(
            obj.title,
            obj.description,
            obj.recipient,
            obj.amountList.fromProtobuf(),
        )

    override fun convert(obj: CommunityPoolSpendProposal) = communityPoolSpendProposal {
        title = obj.title
        description = obj.description
        recipient = obj.recipient
        amount.addAll(obj.amount.toProtobuf())
    }
}
