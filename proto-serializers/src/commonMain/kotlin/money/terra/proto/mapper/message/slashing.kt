package money.terra.proto.mapper.message

import cosmos.slashing.v1beta1.Tx.MsgUnjail
import cosmos.slashing.v1beta1.msgUnjail
import money.terra.message.UnjailMessage
import money.terra.proto.mapper.ProtobufTypeMapper

object UnjailMessageMapper :
    ProtobufTypeMapper<UnjailMessage, MsgUnjail> {

    override val descriptor = MsgUnjail.getDescriptor()

    override val parser = MsgUnjail.parser()

    override fun convert(obj: MsgUnjail) = UnjailMessage(
        obj.validatorAddr,
    )

    override fun convert(obj: UnjailMessage) = msgUnjail {
        validatorAddr = obj.address
    }
}