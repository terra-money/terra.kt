package money.terra.key

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kr.jadekim.common.encoder.encodeHex
import kr.jadekim.common.extension.utf8
import kr.jadekim.common.hash.SHA_256
import money.terra.util.Bip32
import money.terra.util.Bip32KeyPair
import money.terra.util.Mnemonic

interface Key {

    val publicKey: ByteArray

    fun sign(message: ByteArray): Deferred<ByteArray>

    fun sign(message: String): Deferred<ByteArray> = sign(message.utf8())
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

private fun ByteArray.toFixedSize(length: Int) = ByteArray(length).also { copyInto(it) }

private fun mnemonicToKeyPair(mnemonic: String, account: Int, index: Int, coinType: Int): Bip32KeyPair {
    val seed = Mnemonic.seedFrom(mnemonic)
    val hdPathLuna = intArrayOf(44.hard, coinType.hard, account.hard, 0, index)

    return Bip32.keyPair(seed, hdPathLuna)
}

private val Int.hard
    get() = this or -0x80000000
