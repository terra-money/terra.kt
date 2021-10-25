package money.terra.util

import kotlin.jvm.JvmStatic

expect object Bech32 {

    fun encode(hrp: Bech32Hrp, data: ByteArray): String

    fun toWords(data: ByteArray): ByteArray

    fun decode(str: String): Pair<Bech32Hrp, ByteArray>
}

enum class Bech32Hrp(val value: String) {
    ACCOUNT("terra"),
    ACCOUNT_PUBLIC_KEY("terrapub"),
    VALIDATOR_OPERATOR("terravaloper"),
    VALIDATOR_OPERATOR_PUBLIC_KEY("terravaloperpub"),
    CONSENSUS_NODE("terravalcons"),
    CONSENSUS_NODE_PUBLIC_KEY("terravalconspub");

    companion object {

        @JvmStatic
        fun fromHrp(hrp: String): Bech32Hrp? = values().firstOrNull { it.value.equals(hrp, true) }
    }
}