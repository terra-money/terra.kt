package money.terra.proto.mapper.message

import cosmos.bank.v1beta1.*
import cosmos.bank.v1beta1.Tx.MsgMultiSend
import cosmos.bank.v1beta1.Tx.MsgSend
import money.terra.message.MultipleSendMessage
import money.terra.message.SendMessage
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object SendMessageMapper : ProtobufTypeMapper<SendMessage, MsgSend> {

    override val descriptor = MsgSend.getDescriptor()

    override val parser = MsgSend.parser()

    override fun convert(obj: MsgSend) = SendMessage(
        obj.fromAddress,
        obj.toAddress,
        obj.amountList.fromProtobuf(),
    )

    override fun convert(obj: SendMessage) = msgSend {
        fromAddress = obj.fromAddress
        toAddress = obj.toAddress
        amount.addAll(obj.amount.toProtobuf())
    }
}

object MultipleSendMessageMapper : ProtobufTypeMapper<MultipleSendMessage, MsgMultiSend> {

    override val descriptor = MsgMultiSend.getDescriptor()

    override val parser = MsgMultiSend.parser()

    override fun convert(obj: MsgMultiSend) = MultipleSendMessage(
        obj.inputsList.fromProtobufInput(),
        obj.outputsList.fromProtobufOutput(),
    )

    override fun convert(obj: MultipleSendMessage) = msgMultiSend {
        inputs.addAll(obj.inputs.toProtobufInput())
        outputs.addAll(obj.outputs.toProtobufOutput())
    }
}

private fun List<MultipleSendMessage.Amount>.toProtobufInput(): List<Bank.Input> = map {
    input {
        address = it.address
        coins.addAll(it.coins.toProtobuf())
    }
}

private fun List<Bank.Input>.fromProtobufInput(): List<MultipleSendMessage.Amount> = map {
    MultipleSendMessage.Amount(
        it.address,
        it.coinsList.fromProtobuf(),
    )
}

private fun List<MultipleSendMessage.Amount>.toProtobufOutput(): List<Bank.Output> = map {
    output {
        address = it.address
        coins.addAll(it.coins.toProtobuf())
    }
}

private fun List<Bank.Output>.fromProtobufOutput(): List<MultipleSendMessage.Amount> = map {
    MultipleSendMessage.Amount(
        it.address,
        it.coinsList.fromProtobuf(),
    )
}
