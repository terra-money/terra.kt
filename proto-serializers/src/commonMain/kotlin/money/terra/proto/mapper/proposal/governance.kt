package money.terra.proto.mapper.proposal

import cosmos.gov.v1beta1.textProposal
import cosmos.params.v1beta1.paramChange
import cosmos.params.v1beta1.parameterChangeProposal
import money.terra.proposal.ParameterChangeProposal
import money.terra.proposal.TextProposal
import money.terra.proto.mapper.ProtobufTypeMapper

object ParameterChangeProposalMapper :
    ProtobufTypeMapper<ParameterChangeProposal, cosmos.params.v1beta1.Params.ParameterChangeProposal> {

    override val descriptor = cosmos.params.v1beta1.Params.ParameterChangeProposal.getDescriptor()

    override val parser = cosmos.params.v1beta1.Params.ParameterChangeProposal.parser()

    override fun convert(obj: cosmos.params.v1beta1.Params.ParameterChangeProposal) = ParameterChangeProposal(
        obj.title,
        obj.description,
        obj.changesList.map(ChangeMapper::convert),
    )

    override fun convert(obj: ParameterChangeProposal) = parameterChangeProposal {
        title = obj.title
        description = obj.description
        changes.addAll(obj.changes.map(ChangeMapper::convert))
    }

    object ChangeMapper :
        ProtobufTypeMapper<ParameterChangeProposal.Change, cosmos.params.v1beta1.Params.ParamChange> {

        override val descriptor = cosmos.params.v1beta1.Params.ParamChange.getDescriptor()

        override val parser = cosmos.params.v1beta1.Params.ParamChange.parser()

        override fun convert(obj: cosmos.params.v1beta1.Params.ParamChange) = ParameterChangeProposal.Change(
            obj.subspace,
            obj.key,
            obj.value,
        )

        override fun convert(obj: ParameterChangeProposal.Change) = paramChange {
            subspace = obj.subspace
            key = obj.key
            value = obj.value
        }
    }
}

object TextProposalMapper :
    ProtobufTypeMapper<TextProposal, cosmos.gov.v1beta1.Gov.TextProposal> {

    override val descriptor = cosmos.gov.v1beta1.Gov.TextProposal.getDescriptor()

    override val parser = cosmos.gov.v1beta1.Gov.TextProposal.parser()

    override fun convert(obj: cosmos.gov.v1beta1.Gov.TextProposal) = TextProposal(
        obj.title,
        obj.description,
    )

    override fun convert(obj: TextProposal) = textProposal {
        title = obj.title
        description = obj.description
    }
}
