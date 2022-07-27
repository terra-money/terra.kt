package money.terra.proto.mapper.message

import cosmos.distribution.v1beta1.Tx.*
import cosmos.distribution.v1beta1.msgFundCommunityPool
import cosmos.distribution.v1beta1.msgSetWithdrawAddress
import cosmos.distribution.v1beta1.msgWithdrawDelegatorReward
import cosmos.distribution.v1beta1.msgWithdrawValidatorCommission
import money.terra.message.FundCommunityPoolMessage
import money.terra.message.SetWithdrawAddressMessage
import money.terra.message.WithdrawDelegatorRewardMessage
import money.terra.message.WithdrawValidatorCommissionMessage
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object SetWithdrawAddressMessageMapper : ProtobufTypeMapper<SetWithdrawAddressMessage, MsgSetWithdrawAddress> {

    override val descriptor = MsgSetWithdrawAddress.getDescriptor()

    override val parser = MsgSetWithdrawAddress.parser()

    override fun convert(obj: MsgSetWithdrawAddress) = SetWithdrawAddressMessage(
        obj.delegatorAddress,
        obj.withdrawAddress,
    )

    override fun convert(obj: SetWithdrawAddressMessage) = msgSetWithdrawAddress {
        delegatorAddress = obj.delegatorAddress
        withdrawAddress = obj.withdrawAddress
    }

}

object WithdrawDelegatorRewardMessageMapper :
    ProtobufTypeMapper<WithdrawDelegatorRewardMessage, MsgWithdrawDelegatorReward> {

    override val descriptor = MsgWithdrawDelegatorReward.getDescriptor()

    override val parser = MsgWithdrawDelegatorReward.parser()

    override fun convert(obj: MsgWithdrawDelegatorReward) = WithdrawDelegatorRewardMessage(
        obj.delegatorAddress,
        obj.validatorAddress,
    )

    override fun convert(obj: WithdrawDelegatorRewardMessage) = msgWithdrawDelegatorReward {
        delegatorAddress = obj.delegatorAddress
        validatorAddress = obj.validatorAddress
    }

}

object WithdrawValidatorCommissionMessageMapper :
    ProtobufTypeMapper<WithdrawValidatorCommissionMessage, MsgWithdrawValidatorCommission> {

    override val descriptor = MsgWithdrawValidatorCommission.getDescriptor()

    override val parser = MsgWithdrawValidatorCommission.parser()

    override fun convert(obj: MsgWithdrawValidatorCommission) = WithdrawValidatorCommissionMessage(
        obj.validatorAddress,
    )

    override fun convert(obj: WithdrawValidatorCommissionMessage) = msgWithdrawValidatorCommission {
        validatorAddress = obj.validatorAddress
    }
}

object FundCommunityPoolMessageMapper : ProtobufTypeMapper<FundCommunityPoolMessage, MsgFundCommunityPool> {

    override val descriptor = MsgFundCommunityPool.getDescriptor()

    override val parser = MsgFundCommunityPool.parser()

    override fun convert(obj: MsgFundCommunityPool) = FundCommunityPoolMessage(
        obj.amountList.fromProtobuf(),
        obj.depositor,
    )

    override fun convert(obj: FundCommunityPoolMessage) = msgFundCommunityPool {
        amount.addAll(obj.amount.toProtobuf())
        depositor = obj.depositor
    }
}
