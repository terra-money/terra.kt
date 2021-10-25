package money.terra.util

import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom
import java.util.*

actual object Mnemonic {

    private var random: Random = SecureRandom()

    actual fun generate(): String = ByteArray(32)
        .apply { random.nextBytes(this) }
        .let { MnemonicUtils.generateMnemonic(it) }

    actual fun seedFrom(mnemonic: String, passphrase: String?): ByteArray {
        return MnemonicUtils.generateSeed(mnemonic, passphrase)
    }

    fun random(random: Random) {
        Mnemonic.random = random
    }
}