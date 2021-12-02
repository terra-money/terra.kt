package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("core/StdTx")
data class Transaction(
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

        fun build() = Transaction(
            messages.toList(),
            memo,
            fee,
        )
    }
}
