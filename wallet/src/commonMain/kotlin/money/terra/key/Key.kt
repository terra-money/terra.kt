package money.terra.key

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kr.jadekim.common.encoder.decodeBase64
import kr.jadekim.common.encoder.encodeHex
import kr.jadekim.common.extension.utf8
import kr.jadekim.common.hash.SHA_256
import money.terra.util.Bip32
import money.terra.util.Bip32KeyPair
import money.terra.util.Mnemonic
import kotlin.jvm.JvmStatic

interface Key {

    val publicKey: ByteArray

    fun sign(message: ByteArray): Deferred<ByteArray>

    fun sign(message: String): Deferred<ByteArray> = sign(message.utf8())

    fun verify(message: ByteArray, signature: ByteArray): Boolean = Bip32.verify(SHA_256.hash(message), publicKey, signature)

    fun verify(message: String, signature: ByteArray) = verify(message.utf8(), signature)

    fun verify(message: String, signature: String) = verify(message.utf8(), signature.decodeBase64())
}

open class PublicKey(override val publicKey: ByteArray) : Key {

    companion object {

        @JvmStatic
        fun recoverFromSignature(message: ByteArray, signature: ByteArray) = PublicKey(
            Bip32.recoverPublicKey(SHA_256.hash(message), signature)
        )

        @JvmStatic
        fun recoverFromSignature(message: String, signature: ByteArray) = recoverFromSignature(message.utf8(), signature)

        @JvmStatic
        fun recoverFromSignature(message: String, signature: String) = recoverFromSignature(message.utf8(), signature.decodeBase64())
    }

    override fun sign(message: ByteArray): Deferred<ByteArray> {
        throw NotImplementedError()
    }
}

open class RawKey(privateKey: ByteArray, publicKey: ByteArray? = null) : Key {

    internal constructor(keyPair: Bip32KeyPair) : this(keyPair.privateKey, keyPair.publicKey)

    val privateKey: ByteArray = privateKey.toFixedSize(33)
    val privateKeyHex: String = this.privateKey.encodeHex()

    final override val publicKey: ByteArray = publicKey?.toFixedSize(33) ?: Bip32.publicKeyFor(this.privateKey)
    val publicKeyHex: String = this.publicKey.encodeHex()

    fun signSync(message: ByteArray): ByteArray = Bip32.sign(SHA_256.hash(message), privateKey)

    fun signSync(message: String): ByteArray = signSync(message.utf8())

    override fun sign(message: ByteArray): Deferred<ByteArray> = CompletableDeferred(signSync(message))
}

open class MnemonicKey(
    val mnemonic: String,
    val account: Int = 0,
    val index: Int = 0,
    val coinType: Int = COIN_TYPE,
) : RawKey(mnemonicToKeyPair(mnemonic, account, index, coinType)) {

    companion object {
        const val COIN_TYPE = 330
        const val LEGACY_COIN_TYPE = 118

        fun create(account: Int = 0, index: Int = 0) = MnemonicKey(Mnemonic.generate(), account, index)
    }
}

private fun ByteArray.toFixedSize(length: Int) = ByteArray(length) {
    val padLength = length - size

    if (padLength - it > 0) {
        0.toByte()
    } else {
        get(it - padLength)
    }
}

private fun mnemonicToKeyPair(mnemonic: String, account: Int, index: Int, coinType: Int): Bip32KeyPair {
    val seed = Mnemonic.seedFrom(mnemonic)
    val hdPathLuna = intArrayOf(44.hard, coinType.hard, account.hard, 0, index)

    return Bip32.keyPair(seed, hdPathLuna)
}

private val Int.hard
    get() = this or -0x80000000
