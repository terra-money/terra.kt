package money.terra.amino.serializer.message

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import money.terra.amino.serializer.AminoSerializer
import money.terra.amino.serializer.PolymorphicObjectSerializer
import money.terra.message.*
import money.terra.model.Message
import money.terra.proposal.*

val BankSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(SendMessage::class, AminoSerializer(SendMessage.serializer(), "bank/MsgSend"))
        subclass(MultipleSendMessage::class, AminoSerializer(MultipleSendMessage.serializer(), "bank/MsgMultiSend"))
    }
}

val DistributionSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            SetWithdrawAddressMessage::class,
            AminoSerializer(SetWithdrawAddressMessage.serializer(), "distribution/MsgModifyWithdrawAddress"),
        )
        subclass(
            WithdrawDelegatorRewardMessage::class,
            AminoSerializer(WithdrawDelegatorRewardMessage.serializer(), "distribution/MsgWithdrawDelegationReward"),
        )
        subclass(
            WithdrawValidatorCommissionMessage::class,
            AminoSerializer(
                WithdrawValidatorCommissionMessage.serializer(),
                "distribution/MsgWithdrawValidatorCommission",
            ),
        )
        subclass(
            FundCommunityPoolMessage::class,
            AminoSerializer(FundCommunityPoolMessage.serializer(), "distribution/MsgFundCommunityPool"),
        )
    }
}

val FeeGrantSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            GrantAllowanceMessage::class,
            AminoSerializer(GrantAllowanceMessage.serializer(), "feegrant/MsgGrantAllowance"),
        )
        subclass(
            RevokeAllowanceMessage::class,
            AminoSerializer(RevokeAllowanceMessage.serializer(), "feegrant/MsgRevokeAllowance"),
        )
    }

    contextual(Allowance::class, PolymorphicObjectSerializer(Allowance::class))

    polymorphic(Allowance::class) {
        subclass(BasicAllowance::class, AminoSerializer(BasicAllowance.serializer(), "feegrant/BasicAllowance"))
        subclass(
            PeriodicAllowance::class,
            AminoSerializer(PeriodicAllowance.serializer(), "feegrant/PeriodicAllowance"),
        )
        subclass(
            AllowedMsgAllowance::class,
            AminoSerializer(AllowedMsgAllowance.serializer(), "feegrant/AllowedMsgAllowance"),
        )
    }
}

val GovernanceSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            SubmitProposalMessage::class,
            AminoSerializer(SubmitProposalMessage.serializer(), "gov/MsgSubmitProposal"),
        )
        subclass(DepositProposalMessage::class, AminoSerializer(DepositProposalMessage.serializer(), "gov/MsgDeposit"))
        subclass(VoteProposalMessage::class, AminoSerializer(VoteProposalMessage.serializer(), "gov/MsgVote"))
    }

    contextual(Proposal::class, PolymorphicObjectSerializer(Proposal::class))

    polymorphic(Proposal::class) {
        subclass(
            CommunityPoolSpendProposal::class,
            AminoSerializer(CommunityPoolSpendProposal.serializer(), "distribution/CommunityPoolSpendProposal"),
        )
        subclass(
            ParameterChangeProposal::class,
            AminoSerializer(ParameterChangeProposal.serializer(), "gov/ParameterChangeProposal"),
        )
        subclass(TextProposal::class, AminoSerializer(TextProposal.serializer(), "gov/TextProposal"))
        subclass(
            TaxRateUpdateProposal::class,
            AminoSerializer(TaxRateUpdateProposal.serializer(), "treasury/TaxRateUpdateProposal"),
        )
        subclass(
            RewardWeightUpdateProposal::class,
            AminoSerializer(RewardWeightUpdateProposal.serializer(), "treasury/RewardWeightUpdateProposal"),
        )
    }
}

val MessageAuthSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            GrantAuthorizationMessage::class,
            AminoSerializer(GrantAuthorizationMessage.serializer(), "msgauth/MsgGrantAuthorization"),
        )
        subclass(
            ExecuteAuthorizedMessage::class,
            AminoSerializer(ExecuteAuthorizedMessage.serializer(), "msgauth/MsgExecAuthorized"),
        )
        subclass(
            RevokeAuthorizationMessage::class,
            AminoSerializer(RevokeAuthorizationMessage.serializer(), "msgauth/MsgRevokeAuthorization"),
        )
    }

    contextual(Authorization::class, PolymorphicObjectSerializer(Authorization::class))

    polymorphic(Authorization::class) {
        subclass(
            GenericAuthorization::class,
            AminoSerializer(GenericAuthorization.serializer(), "msgauth/GenericAuthorization"),
        )
        subclass(SendAuthorization::class, AminoSerializer(SendAuthorization.serializer(), "msgauth/SendAuthorization"))
        subclass(
            StakeAuthorization::class,
            AminoSerializer(StakeAuthorization.serializer(), "msgauth/StakeAuthorization"),
        )
    }
}

val SlashingSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(UnjailMessage::class, AminoSerializer(UnjailMessage.serializer(), "cosmos/MsgUnjail"))
    }
}

val StakingSerializerModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(DelegateMessage::class, AminoSerializer(DelegateMessage.serializer(), "staking/MsgDelegate"))
        subclass(UndelegateMessage::class, AminoSerializer(UndelegateMessage.serializer(), "staking/MsgUndelegate"))
        subclass(
            BeginRedelegateMessage::class,
            AminoSerializer(BeginRedelegateMessage.serializer(), "staking/MsgBeginRedelegate"),
        )
        subclass(
            EditValidatorMessage::class,
            AminoSerializer(EditValidatorMessage.serializer(), "staking/MsgEditValidator"),
        )
        subclass(
            CreateValidatorMessage::class,
            AminoSerializer(CreateValidatorMessage.serializer(), "staking/MsgCreateValidator"),
        )
    }
}

val WasmSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(StoreCodeMessage::class, AminoSerializer(StoreCodeMessage.serializer(), "wasm/MsgStoreCode"))
        subclass(
            InstantiateContractMessage::class,
            AminoSerializer(InstantiateContractMessage.serializer(), "wasm/MsgInstantiateContract"),
        )
        subclass(
            ExecuteContractMessage::class,
            AminoSerializer(ExecuteContractMessage.serializer(), "wasm/MsgExecuteContract"),
        )
//        subclass(
//            JsonExecuteContractMessage::class,
//            AminoSerializer(JsonExecuteContractMessage.serializer(), "wasm/MsgExecuteContract"),
//        )
        subclass(
            MigrateContractMessage::class,
            AminoSerializer(MigrateContractMessage.serializer(), "wasm/MsgMigrateContract"),
        )
        subclass(
            UpdateContractAdminMessage::class,
            AminoSerializer(UpdateContractAdminMessage.serializer(), "wasm/MsgUpdateContractAdmin"),
        )
    }
}

val MessageSerializersModule = BankSerializersModule + DistributionSerializersModule + FeeGrantSerializersModule +
        GovernanceSerializersModule + MessageAuthSerializersModule + SlashingSerializersModule +
        StakingSerializerModule + WasmSerializersModule
