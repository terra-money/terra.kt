package money.terra.util

import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Sign
import java.math.BigInteger

actual object Bip32 {

    actual fun keyPair(seed: ByteArray, hdPath: IntArray): Bip32KeyPair {
        val masterKey = Bip32ECKeyPair.generateKeyPair(seed)
        val terraHD = Bip32ECKeyPair.deriveKeyPair(masterKey, hdPath)
        val privateKey = terraHD.privateKeyBytes33
        val publicKey = terraHD.publicKeyPoint.getEncoded(true)

        return Bip32KeyPair(publicKey, privateKey)
    }

    actual fun publicKeyFor(privateKey: ByteArray): ByteArray {
        return Sign.publicPointFromPrivate(BigInteger(1, privateKey)).getEncoded(true)
    }

    actual fun sign(messageHash: ByteArray, privateKey: ByteArray): ByteArray {
        val keyPair = Bip32ECKeyPair.create(privateKey)

        val signature = Sign.signMessage(messageHash, keyPair, false)
        val r = signature.r
        val s = signature.s

        var index = 0
        val start = if (r.size > 32) r.size - 32 else 0

        val result = ByteArray(r.size + s.size - start)

        for (i in start until r.size) {
            result[index++] = r[i]
        }

        for (i in s.indices) {
            result[index++] = s[i]
        }

        return result
    }
}