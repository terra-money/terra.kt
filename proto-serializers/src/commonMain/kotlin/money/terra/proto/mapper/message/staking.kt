package money.terra.proto.mapper.message

import cosmos.staking.v1beta1.*
import cosmos.staking.v1beta1.Staking.Description
import cosmos.staking.v1beta1.Tx.*
import money.terra.message.*
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.message.CreateValidatorMessageMapper.CommissionRatesMapper.fromProtobuf
import money.terra.proto.mapper.message.CreateValidatorMessageMapper.CommissionRatesMapper.toProtobuf
import money.terra.proto.mapper.message.ValidatorDescriptionDataMapper.fromProtobuf
import money.terra.proto.mapper.message.ValidatorDescriptionDataMapper.toProtobuf
import money.terra.proto.mapper.type.CoinMapper.fromProtobuf
import money.terra.proto.mapper.type.CoinMapper.toProtobuf
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object DelegateMessageMapper :
    ProtobufTypeMapper<DelegateMessage, MsgDelegate> {

    override val descriptor = MsgDelegate.getDescriptor()

    override val parser = MsgDelegate.parser()

    override fun convert(obj: MsgDelegate) = DelegateMessage(
        obj.delegatorAddress,
        obj.validatorAddress,
        obj.amount.fromProtobuf(),
    )

    override fun convert(obj: DelegateMessage) = msgDelegate {
        delegatorAddress = obj.delegatorAddress
        validatorAddress = obj.validatorAddress
        amount = obj.amount.toProtobuf()
    }
}

object UndelegateMessageMapper :
    ProtobufTypeMapper<UndelegateMessage, MsgUndelegate> {

    override val descriptor = MsgUndelegate.getDescriptor()

    override val parser = MsgUndelegate.parser()

    override fun convert(obj: MsgUndelegate) = UndelegateMessage(
        obj.delegatorAddress,
        obj.validatorAddress,
        obj.amount.fromProtobuf(),
    )

    override fun convert(obj: UndelegateMessage) = msgUndelegate {
        delegatorAddress = obj.delegatorAddress
        validatorAddress = obj.validatorAddress
        amount = obj.amount.toProtobuf()
    }
}

object BeginRedelegateMessageMapper :
    ProtobufTypeMapper<BeginRedelegateMessage, MsgBeginRedelegate> {

    override val descriptor = MsgBeginRedelegate.getDescriptor()

    override val parser = MsgBeginRedelegate.parser()

    override fun convert(obj: MsgBeginRedelegate) = BeginRedelegateMessage(
        obj.delegatorAddress,
        obj.validatorSrcAddress,
        obj.validatorDstAddress,
        obj.amount.fromProtobuf(),
    )

    override fun convert(obj: BeginRedelegateMessage) = msgBeginRedelegate {
        delegatorAddress = obj.delegatorAddress
        validatorSrcAddress = obj.sourceAddress
        validatorDstAddress = obj.destinationAddress
        amount = obj.amount.toProtobuf()
    }
}

object ValidatorDescriptionDataMapper :
    ProtobufTypeMapper<ValidatorDescriptionData, Description> {

    override val descriptor = Description.getDescriptor()

    override val parser = Description.parser()

    override fun convert(obj: Description) = ValidatorDescriptionData(
        obj.moniker,
        obj.identity,
        obj.website,
        obj.details,
    )

    override fun convert(obj: ValidatorDescriptionData) = description {
        moniker = obj.moniker
        identity = obj.identity
        website = obj.website
        details = obj.details
    }
}

object EditValidatorMessageMapper :
    ProtobufTypeMapper<EditValidatorMessage, MsgEditValidator> {

    override val descriptor = MsgEditValidator.getDescriptor()

    override val parser = MsgEditValidator.parser()

    override fun convert(obj: MsgEditValidator) = EditValidatorMessage(
        obj.description.fromProtobuf(),
        obj.validatorAddress,
        obj.commissionRate,
        obj.minSelfDelegation,
    )

    override fun convert(obj: EditValidatorMessage) = msgEditValidator {
        description = obj.description.toProtobuf()
        validatorAddress = obj.address
        commissionRate = obj.commissionRate
        minSelfDelegation = obj.minSelfDelegation
    }
}

object CreateValidatorMessageMapper :
    ProtobufTypeMapper<CreateValidatorMessage, MsgCreateValidator> {

    override val descriptor = MsgCreateValidator.getDescriptor()

    override val parser = MsgCreateValidator.parser()

    override fun convert(obj: MsgCreateValidator) = CreateValidatorMessage(
        obj.description.fromProtobuf(),
        obj.commission.fromProtobuf(),
        obj.minSelfDelegation,
        obj.delegatorAddress,
        obj.validatorAddress,
        ProtobufFormat.decodeFromAny(obj.pubkey),
        obj.value.fromProtobuf(),
    )

    override fun convert(obj: CreateValidatorMessage) = msgCreateValidator {
        description = obj.description.toProtobuf()
        commission = obj.commission.toProtobuf()
        delegatorAddress = obj.delegatorAddress
        validatorAddress = obj.validatorAddress
        minSelfDelegation = obj.minSelfDelegation
        pubkey = ProtobufFormat.encodeToAny(obj.publicKey)
        value = obj.value.toProtobuf()
    }

    object CommissionRatesMapper :
        ProtobufTypeMapper<CreateValidatorMessage.CommissionRates, Staking.CommissionRates> {

        override val descriptor = Staking.CommissionRates.getDescriptor()

        override val parser = Staking.CommissionRates.parser()

        override fun convert(obj: Staking.CommissionRates) = CreateValidatorMessage.CommissionRates(
            obj.rate,
            obj.maxRate,
            obj.maxChangeRate,
        )

        override fun convert(obj: CreateValidatorMessage.CommissionRates) = commissionRates {
            rate = obj.rate
            maxRate = obj.maxRate
            maxChangeRate = obj.maxChangeRate
        }
    }
}
