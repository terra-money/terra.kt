package money.terra.proto.mapper.message

import cosmos.authz.v1beta1.*
import cosmos.authz.v1beta1.Authz.Grant
import cosmos.authz.v1beta1.Tx.*
import cosmos.bank.v1beta1.sendAuthorization
import cosmos.staking.v1beta1.Authz.AuthorizationType
import cosmos.staking.v1beta1.Authz.StakeAuthorization.Validators
import cosmos.staking.v1beta1.StakeAuthorizationKt
import cosmos.staking.v1beta1.stakeAuthorization
import money.terra.message.*
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.message.MessageGrantMapper.fromProtobuf
import money.terra.proto.mapper.message.MessageGrantMapper.toProtobuf
import money.terra.proto.mapper.type.CoinMapper.fromProtobuf
import money.terra.proto.mapper.type.CoinMapper.toProtobuf
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object GrantAuthorizationMessageMapper :
    ProtobufTypeMapper<GrantAuthorizationMessage, MsgGrant> {

    override val descriptor = MsgGrant.getDescriptor()

    override val parser = MsgGrant.parser()

    override fun convert(obj: MsgGrant) = GrantAuthorizationMessage(
        obj.granter,
        obj.grantee,
        obj.grant.fromProtobuf(),
    )

    override fun convert(obj: GrantAuthorizationMessage) = msgGrant {
        granter = obj.granter
        grantee = obj.grantee
        grant = obj.grant.toProtobuf()
    }
}

object ExecuteAuthorizedMessageMapper :
    ProtobufTypeMapper<ExecuteAuthorizedMessage, MsgExec> {

    override val descriptor = MsgExec.getDescriptor()

    override val parser = MsgExec.parser()

    override fun convert(obj: MsgExec) = ExecuteAuthorizedMessage(
        obj.grantee,
        obj.msgsList.map { ProtobufFormat.decodeFromAny(it) }
    )

    override fun convert(obj: ExecuteAuthorizedMessage) = msgExec {
        grantee = obj.grantee
        msgs.addAll(obj.messages.map { ProtobufFormat.encodeToAny(it) })
    }
}

object RevokeAuthorizationMessageMapper :
    ProtobufTypeMapper<RevokeAuthorizationMessage, MsgRevoke> {

    override val descriptor = MsgRevoke.getDescriptor()

    override val parser = MsgRevoke.parser()

    override fun convert(obj: MsgRevoke) = RevokeAuthorizationMessage(
        obj.granter,
        obj.grantee,
        obj.msgTypeUrl,
    )

    override fun convert(obj: RevokeAuthorizationMessage) = msgRevoke {
        granter = obj.granter
        grantee = obj.grantee
        msgTypeUrl = obj.messageType
    }
}

object MessageGrantMapper :
    ProtobufTypeMapper<MessageGrant, Grant> {

    override val descriptor = Grant.getDescriptor()

    override val parser = Grant.parser()

    override fun convert(obj: Grant) = MessageGrant(
        ProtobufFormat.decodeFromAny(obj.authorization),
        obj.expiration.fromProtobuf()
    )

    override fun convert(obj: MessageGrant) = grant {
        authorization = ProtobufFormat.encodeToAny(obj.authorization)
        expiration = obj.expiration.toProtobuf()
    }
}

object GenericAuthorizationMapper :
    ProtobufTypeMapper<GenericAuthorization, cosmos.authz.v1beta1.Authz.GenericAuthorization> {

    override val descriptor = cosmos.authz.v1beta1.Authz.GenericAuthorization.getDescriptor()

    override val parser = cosmos.authz.v1beta1.Authz.GenericAuthorization.parser()

    override fun convert(obj: cosmos.authz.v1beta1.Authz.GenericAuthorization) = GenericAuthorization(
        obj.msg,
    )

    override fun convert(obj: GenericAuthorization) = genericAuthorization {
        msg = obj.message
    }
}

object SendAuthorizationMapper :
    ProtobufTypeMapper<SendAuthorization, cosmos.bank.v1beta1.Authz.SendAuthorization> {

    override val descriptor = cosmos.bank.v1beta1.Authz.SendAuthorization.getDescriptor()

    override val parser = cosmos.bank.v1beta1.Authz.SendAuthorization.parser()

    override fun convert(obj: cosmos.bank.v1beta1.Authz.SendAuthorization) = SendAuthorization(
        obj.spendLimitList.fromProtobuf(),
    )

    override fun convert(obj: SendAuthorization) = sendAuthorization {
        spendLimit.addAll(obj.spendLimit.toProtobuf())
    }
}

object StakeAuthorizationMapper :
    ProtobufTypeMapper<StakeAuthorization, cosmos.staking.v1beta1.Authz.StakeAuthorization> {

    override val descriptor = cosmos.staking.v1beta1.Authz.StakeAuthorization.getDescriptor()

    override val parser = cosmos.staking.v1beta1.Authz.StakeAuthorization.parser()

    override fun convert(obj: cosmos.staking.v1beta1.Authz.StakeAuthorization) = StakeAuthorization(
        obj.maxTokens.fromProtobuf(),
        if (obj.hasAllowList()) {
            StakeAuthorization.Validators.allowListOf(obj.allowList.addressList)
        } else {
            StakeAuthorization.Validators.denyListOf(obj.denyList.addressList)
        },
        obj.authorizationType.fromProtobuf(),
    )

    override fun convert(obj: StakeAuthorization) = stakeAuthorization {
        maxTokens = obj.maxTokens.toProtobuf()
        authorizationType = obj.authorizationType.toProtobuf()

        if (obj.validators.allowList != null) {
            allowList = obj.validators.allowList!!.toProtobuf()
        } else if (obj.validators.denyList != null) {
            denyList = obj.validators.denyList!!.toProtobuf()
        }
    }
}

fun StakeAuthorization.Validators.ListWrapper.toProtobuf(): Validators = StakeAuthorizationKt.validators {
    address.addAll(this@toProtobuf.address)
}

fun Validators.fromProtobuf() = StakeAuthorization.Validators.ListWrapper(addressList)

fun StakeAuthorization.AuthorizationType.toProtobuf(): AuthorizationType = when (this) {
    StakeAuthorization.AuthorizationType.DELEGATE -> AuthorizationType.AUTHORIZATION_TYPE_DELEGATE
    StakeAuthorization.AuthorizationType.UNSPECIFIED -> AuthorizationType.AUTHORIZATION_TYPE_UNSPECIFIED
    StakeAuthorization.AuthorizationType.UNDELEGATE -> AuthorizationType.AUTHORIZATION_TYPE_UNDELEGATE
    StakeAuthorization.AuthorizationType.REDELEGATE -> AuthorizationType.AUTHORIZATION_TYPE_REDELEGATE
}

fun AuthorizationType.fromProtobuf(): StakeAuthorization.AuthorizationType = when (this) {
    AuthorizationType.AUTHORIZATION_TYPE_DELEGATE -> StakeAuthorization.AuthorizationType.DELEGATE
    AuthorizationType.AUTHORIZATION_TYPE_UNDELEGATE -> StakeAuthorization.AuthorizationType.UNDELEGATE
    AuthorizationType.AUTHORIZATION_TYPE_REDELEGATE -> StakeAuthorization.AuthorizationType.REDELEGATE
    AuthorizationType.AUTHORIZATION_TYPE_UNSPECIFIED -> StakeAuthorization.AuthorizationType.UNSPECIFIED
    AuthorizationType.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized AuthorizationType")
}
