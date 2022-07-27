package money.terra.proto.mapper.message

import cosmos.feegrant.v1beta1.*
import cosmos.feegrant.v1beta1.Tx.MsgGrantAllowance
import cosmos.feegrant.v1beta1.Tx.MsgRevokeAllowance
import money.terra.message.*
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.message.BasicAllowanceMapper.fromProtobuf
import money.terra.proto.mapper.message.BasicAllowanceMapper.toProtobuf
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object GrantAllowanceMessageMapper :
    ProtobufTypeMapper<GrantAllowanceMessage, MsgGrantAllowance> {

    override val descriptor = MsgGrantAllowance.getDescriptor()

    override val parser = MsgGrantAllowance.parser()

    override fun convert(obj: MsgGrantAllowance) = GrantAllowanceMessage(
        obj.granter,
        obj.grantee,
        ProtobufFormat.decodeFromAny(obj.allowance),
    )

    override fun convert(obj: GrantAllowanceMessage) = msgGrantAllowance {
        granter = obj.granter
        grantee = obj.grantee
        allowance = ProtobufFormat.encodeToAny(obj.allowance)
    }
}

object RevokeAllowanceMessageMapper :
    ProtobufTypeMapper<RevokeAllowanceMessage, MsgRevokeAllowance> {

    override val descriptor = MsgRevokeAllowance.getDescriptor()

    override val parser = MsgRevokeAllowance.parser()

    override fun convert(obj: MsgRevokeAllowance) = RevokeAllowanceMessage(
        obj.granter,
        obj.grantee,
    )

    override fun convert(obj: RevokeAllowanceMessage) = msgRevokeAllowance {
        granter = obj.granter
        grantee = obj.grantee
    }
}

object BasicAllowanceMapper :
    ProtobufTypeMapper<BasicAllowance, Feegrant.BasicAllowance> {

    override val descriptor = Feegrant.BasicAllowance.getDescriptor()

    override val parser = Feegrant.BasicAllowance.parser()

    override fun convert(obj: Feegrant.BasicAllowance) = BasicAllowance(
        obj.spendLimitList.fromProtobuf(),
        obj.expiration.fromProtobuf(),
    )

    override fun convert(obj: BasicAllowance) = basicAllowance {
        spendLimit.addAll(obj.spendLimit.toProtobuf())
        expiration = obj.expiration.toProtobuf()
    }
}

object PeriodicAllowanceMapper :
    ProtobufTypeMapper<PeriodicAllowance, Feegrant.PeriodicAllowance> {

    override val descriptor = Feegrant.PeriodicAllowance.getDescriptor()

    override val parser = Feegrant.PeriodicAllowance.parser()

    override fun convert(obj: Feegrant.PeriodicAllowance) = PeriodicAllowance(
        obj.basic.fromProtobuf(),
        obj.period.fromProtobuf(),
        obj.periodSpendLimitList.fromProtobuf(),
        obj.periodCanSpendList.fromProtobuf(),
        obj.periodReset.fromProtobuf(),
    )

    override fun convert(obj: PeriodicAllowance) = periodicAllowance {
        basic = obj.basic.toProtobuf()
        period = obj.period.toProtobuf()
        periodSpendLimit.addAll(obj.periodSpendLimit.toProtobuf())
        periodCanSpend.addAll(obj.periodCanSpend.toProtobuf())
        periodReset = obj.periodReset.toProtobuf()
    }
}

object AllowedMsgAllowanceMapper :
    ProtobufTypeMapper<AllowedMsgAllowance, Feegrant.AllowedMsgAllowance> {

    override val descriptor = Feegrant.AllowedMsgAllowance.getDescriptor()

    override val parser = Feegrant.AllowedMsgAllowance.parser()

    override fun convert(obj: Feegrant.AllowedMsgAllowance) = AllowedMsgAllowance(
        ProtobufFormat.decodeFromAny(obj.allowance),
        obj.allowedMessagesList,
    )

    override fun convert(obj: AllowedMsgAllowance) = allowedMsgAllowance {
        allowance = ProtobufFormat.encodeToAny(obj.allowance)
        allowedMessages.addAll(obj.allowedMessages)
    }
}
