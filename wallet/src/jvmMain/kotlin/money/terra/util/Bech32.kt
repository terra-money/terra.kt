package money.terra.util

import org.bitcoinj.core.Bech32 as Bech32Lib

actual object Bech32 {

    actual fun encode(hrp: Bech32Hrp, data: ByteArray): String = Bech32Lib.encode(hrp.value, data)

    actual fun toWords(data: ByteArray): ByteArray = Bech32Lib.toWords(data)

    actual fun decode(str: String): Pair<Bech32Hrp, ByteArray> {
        val result = Bech32Lib.decode(str)
        val hrp = Bech32Hrp.fromHrp(result.hrp) ?: throw IllegalArgumentException("Unknown hrp ${result.hrp}")

        return hrp to result.data
    }
}