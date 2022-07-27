package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.type.Binary
import money.terra.type.CompactBitArray
import java.util.BitSet
import kotlin.jvm.JvmStatic

@Serializable
data class Transaction(
    val body: TransactionBody,
    val authInfo: AuthInfo,
    val signatures: List<@Contextual Binary>,
)

@Serializable
data class TransactionBody(
    val messages: List<@Contextual Message>,
    val memo: String = "",
    @SerialName("timeout_height") val timeoutHeight: Long = 0,
    @SerialName("extension_options") val extensionOptions: List<ExtensionOption> = emptyList(),
    @SerialName("non_critical_extension_options") val nonCriticalExtensionOptions: List<ExtensionOption> = emptyList(),
)

interface ExtensionOption

@Serializable
data class AuthInfo(
    val signerInfos: List<Signer>,
    val fee: Fee,
) {

    companion object {
        val EMPTY = AuthInfo(emptyList(), Fee.EMPTY)
    }
}

@Serializable
data class Signer(
    @SerialName("public_key") @Contextual val publicKey: PublicKey,
    @SerialName("mode_info") val modeInfo: SignMode,
    val sequence: Long,
)

@Serializable
sealed class SignMode private constructor(){

    @Serializable
    object Unspecified : SignMode()

    @Serializable
    object Direct : SignMode()

    @Serializable
    object Textual : SignMode()

    @Serializable
    object LegacyAminoJson : SignMode()

    @Serializable
    object Eip191 : SignMode()

    @Serializable
    class Multiple(
        @SerialName("bitarray") @Contextual val bitArray: CompactBitArray,
        @SerialName("mode_infos") val modeInfos: List<SignMode>,
    ) : SignMode()
}

@Serializable
data class Tip(
    val amount: List<Coin>,
    val tipper: String,
)

@Serializable
@SerialName("core/StdTx")
@Deprecated("Legacy Tx Format")
data class StdTx(
    @SerialName("msg") val messages: List<@Contextual Message>,
    val memo: String = "",
    val fee: Fee? = null,
    val signatures: List<Signature>? = null,
    @SerialName("timeout_height") @Serializable(LongAsStringSerializer::class) val timeoutHeight: Long = 0,
) {
    val isSigned: Boolean
        get() = signatures?.isNotEmpty() == true

    companion object {

        @JvmStatic
        fun builder() = Builder()
    }

    fun toModel(senderAddress: String) = Transaction(
        TransactionBody(messages, memo, timeoutHeight),
        AuthInfo(
            signatures?.map { Signer(it.publicKey, SignMode.LegacyAminoJson, it.sequence ?: 0) } ?: emptyList(),
            Fee(fee?.gasLimit ?: 0, fee?.feeAmount ?: emptyList(), senderAddress),
        ),
        signatures?.map { it.signature } ?: emptyList(),
    )

    class Builder {

        private var fee: Fee? = null
        private var memo: String = ""
        private var messages: MutableList<Message> = mutableListOf()

        fun fee(fee: Fee): Builder {
            this.fee = fee

            return this
        }

        fun memo(memo: String): Builder {
            this.memo = memo

            return this
        }

        fun message(vararg message: Message): Builder {
            messages.addAll(message)

            return this
        }

        fun message(messages: List<Message>): Builder {
            this.messages.addAll(messages)

            return this
        }

        fun Message.withThis() {
            messages.add(this)
        }

        fun build() = StdTx(
            messages.toList(),
            memo,
            fee,
        )
    }
}
