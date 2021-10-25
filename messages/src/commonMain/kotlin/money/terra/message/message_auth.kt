package money.terra.message

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import money.terra.model.Coin
import money.terra.model.Message
import kotlin.jvm.JvmStatic

@Serializable
class GrantAuthorizationMessage(
    val granter: String,
    val grantee: String,
    val grant: MessageGrant,
) : Message()

@Serializable
class ExecuteAuthorizedMessage(
    val grantee: String,
    @SerialName("msgs") val messages: List<@Contextual Message>,
) : Message()

@Serializable
data class RevokeAuthorizationMessage(
    val granter: String,
    val grantee: String,
    @SerialName("msg_type_url") val messageType: String,
) : Message()

@Serializable
data class MessageGrant(
    @Contextual val authorization: Authorization,
    val expiration: LocalDateTime,
)

abstract class Authorization

@Serializable
data class GenericAuthorization(
    @SerialName("msg") val message: String,
) : Authorization()

@Serializable
data class SendAuthorization(
    @SerialName("spend_limit") val spendLimit: List<Coin>,
) : Authorization()

@Serializable
data class StakeAuthorization(
    @SerialName("max_tokens") val maxTokens: List<Coin>,
    val validators: Validators,
    @SerialName("authorization_type") val authorizationType: AuthorizationType,
) : Authorization() {

    @Serializable
    data class Validators internal constructor(
        @SerialName("allow_list") val allowList: ListWrapper? = null,
        @SerialName("deny_list") val denyList: ListWrapper? = null,
    ) {

        companion object {

            @JvmStatic
            fun allowListOf(validators: List<String>) = Validators(
                allowList = ListWrapper(validators),
            )

            @JvmStatic
            fun denyListOf(validators: List<String>) = Validators(
                denyList = ListWrapper(validators)
            )
        }

        @Serializable
        data class ListWrapper(
            val address: List<String>,
        )
    }

    @Serializable(AuthorizationType.Serializer::class)
    enum class AuthorizationType {
        UNSPECIFIED,
        DELEGATE,
        UNDELEGATE,
        REDELEGATE;

        object Serializer : KSerializer<AuthorizationType> {

            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("AuthorizationType", PrimitiveKind.INT)

            override fun serialize(encoder: Encoder, value: AuthorizationType) {
                encoder.encodeInt(value.ordinal)
            }

            override fun deserialize(decoder: Decoder): AuthorizationType = values()[decoder.decodeInt()]
        }
    }
}
