package money.terra.proto.serializer.message

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import money.terra.message.Allowance
import money.terra.message.Authorization
import money.terra.message.Proposal
import money.terra.model.Message
import money.terra.proto.mapper.mapper
import money.terra.proto.mapper.message.*
import money.terra.proto.mapper.proposal.CommunityPoolSpendProposalMapper
import money.terra.proto.mapper.proposal.ParameterChangeProposalMapper
import money.terra.proto.mapper.proposal.TextProposalMapper
import money.terra.proto.serializer.ProtobufAnySerializer

val BankSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(SendMessageMapper)
        mapper(MultipleSendMessageMapper)
    }
}

val DistributionSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(SetWithdrawAddressMessageMapper)
        mapper(WithdrawDelegatorRewardMessageMapper)
        mapper(WithdrawValidatorCommissionMessageMapper)
        mapper(FundCommunityPoolMessageMapper)
    }
}

val FeeGrantSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(GrantAllowanceMessageMapper)
        mapper(RevokeAllowanceMessageMapper)
    }

    contextual(Allowance::class, ProtobufAnySerializer(Allowance::class))

    polymorphic(Allowance::class) {
        mapper(BasicAllowanceMapper)
        mapper(PeriodicAllowanceMapper)
        mapper(AllowedMsgAllowanceMapper)
    }
}

val GovernanceSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(SubmitProposalMessageMapper)
        mapper(DepositProposalMessageMapper)
        mapper(VoteProposalMessageMapper)
    }

    contextual(Proposal::class, ProtobufAnySerializer(Proposal::class))

    polymorphic(Proposal::class) {
        mapper(CommunityPoolSpendProposalMapper)
        mapper(ParameterChangeProposalMapper)
        mapper(TextProposalMapper)
    }
}

val MessageAuthSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(GrantAuthorizationMessageMapper)
        mapper(ExecuteAuthorizedMessageMapper)
        mapper(RevokeAuthorizationMessageMapper)
    }

    contextual(Authorization::class, ProtobufAnySerializer(Authorization::class))

    polymorphic(Authorization::class) {
        mapper(GenericAuthorizationMapper)
        mapper(SendAuthorizationMapper)
        mapper(StakeAuthorizationMapper)
    }
}

val SlashingSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(UnjailMessageMapper)
    }
}

val StakingSerializerModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(DelegateMessageMapper)
        mapper(UndelegateMessageMapper)
        mapper(BeginRedelegateMessageMapper)
        mapper(EditValidatorMessageMapper)
        mapper(CreateValidatorMessageMapper)
    }
}

val WasmSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        mapper(StoreCodeMessageMapper)
        mapper(InstantiateContractMessageMapper)
        mapper(ExecuteContractMessageMapper)
        mapper(MigrateContractMessageMapper)
        mapper(UpdateContractAdminMessageMapper)
    }
}

val MessageSerializersModule = BankSerializersModule + DistributionSerializersModule + FeeGrantSerializersModule +
        GovernanceSerializersModule + MessageAuthSerializersModule + SlashingSerializersModule +
        StakingSerializerModule + WasmSerializersModule
