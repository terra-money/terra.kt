package money.terra.proto.mapper.message

import cosmos.gov.v1beta1.Gov.VoteOption
import cosmos.gov.v1beta1.Tx.*
import cosmos.gov.v1beta1.msgDeposit
import cosmos.gov.v1beta1.msgSubmitProposal
import cosmos.gov.v1beta1.msgVote
import money.terra.message.DepositProposalMessage
import money.terra.message.SubmitProposalMessage
import money.terra.message.VoteProposalMessage
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object SubmitProposalMessageMapper :
    ProtobufTypeMapper<SubmitProposalMessage, MsgSubmitProposal> {

    override val descriptor = MsgSubmitProposal.getDescriptor()

    override val parser = MsgSubmitProposal.parser()

    override fun convert(obj: MsgSubmitProposal) = SubmitProposalMessage(
        ProtobufFormat.decodeFromAny(obj.content),
        obj.initialDepositList.fromProtobuf(),
        obj.proposer,
    )

    override fun convert(obj: SubmitProposalMessage) = msgSubmitProposal {
        content = ProtobufFormat.encodeToAny(obj.content)
        initialDeposit.addAll(obj.initialDeposit.toProtobuf())
        proposer = obj.proposer
    }
}

object DepositProposalMessageMapper :
    ProtobufTypeMapper<DepositProposalMessage, MsgDeposit> {

    override val descriptor = MsgDeposit.getDescriptor()

    override val parser = MsgDeposit.parser()

    override fun convert(obj: MsgDeposit) = DepositProposalMessage(
        obj.proposalId,
        obj.depositor,
        obj.amountList.fromProtobuf(),
    )

    override fun convert(obj: DepositProposalMessage) = msgDeposit {
        proposalId = obj.proposalId
        depositor = obj.depositor
        amount.addAll(obj.amount.toProtobuf())
    }
}

object VoteProposalMessageMapper :
    ProtobufTypeMapper<VoteProposalMessage, MsgVote> {

    override val descriptor = MsgVote.getDescriptor()

    override val parser = MsgVote.parser()

    override fun convert(obj: MsgVote) = VoteProposalMessage(
        obj.proposalId,
        obj.voter,
        obj.option.fromProtobuf(),
    )

    override fun convert(obj: VoteProposalMessage) = msgVote {
        proposalId = obj.proposalId
        voter = obj.voter
        option = obj.option.toProtobuf()
    }
}

fun VoteProposalMessage.VoteOption.toProtobuf(): VoteOption = when (this) {
    VoteProposalMessage.VoteOption.YES -> VoteOption.VOTE_OPTION_YES
    VoteProposalMessage.VoteOption.ABSTAIN -> VoteOption.VOTE_OPTION_ABSTAIN
    VoteProposalMessage.VoteOption.NO -> VoteOption.VOTE_OPTION_NO
    VoteProposalMessage.VoteOption.NO_WITH_VETO -> VoteOption.VOTE_OPTION_NO_WITH_VETO
    VoteProposalMessage.VoteOption.UNSPECIFIED -> VoteOption.VOTE_OPTION_UNSPECIFIED
}

fun VoteOption.fromProtobuf() = when (this) {
    VoteOption.VOTE_OPTION_YES -> VoteProposalMessage.VoteOption.YES
    VoteOption.VOTE_OPTION_ABSTAIN -> VoteProposalMessage.VoteOption.ABSTAIN
    VoteOption.VOTE_OPTION_NO -> VoteProposalMessage.VoteOption.NO
    VoteOption.VOTE_OPTION_NO_WITH_VETO -> VoteProposalMessage.VoteOption.NO_WITH_VETO
    VoteOption.VOTE_OPTION_UNSPECIFIED -> VoteProposalMessage.VoteOption.UNSPECIFIED
    VoteOption.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized VoteOption")
}
