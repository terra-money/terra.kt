package money.terra.wallet

import kr.jadekim.common.encoder.HEX
import kr.jadekim.common.encoder.decodeHex
import kr.jadekim.common.hash.SHA_256
import money.terra.key.Key
import money.terra.key.MnemonicKey
import money.terra.key.MnemonicKey.Companion.COIN_TYPE
import money.terra.key.PublicKey
import money.terra.key.RawKey
import money.terra.util.Bech32
import money.terra.util.Bech32Hrp
import money.terra.util.Ripemd160
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmOverloads

interface TerraWallet {

    val address: String
    val key: Key?

    companion object {

        @JvmStatic
        @JvmOverloads
        fun create(account: Int = 0, index: Int = 0): Pair<TerraWallet, MnemonicKey> {
            val key = MnemonicKey.create(account, index)

            return TerraWallet(key) to key
        }

        @JvmStatic
        @JvmOverloads
        fun fromMnemonic(
            mnemonic: String,
            account: Int = 0,
            index: Int = 0,
            coinType: Int = COIN_TYPE,
        ) = TerraWallet(MnemonicKey(mnemonic, account, index, coinType))

        @JvmStatic
        @JvmOverloads
        fun fromRawKey(privateKey: ByteArray, publicKey: ByteArray? = null) = TerraWallet(RawKey(privateKey, publicKey))

        @JvmStatic
        @JvmOverloads
        fun fromRawKey(
            privateKey: String,
            publicKey: String? = null,
        ) = fromRawKey(privateKey.decodeHex(), publicKey?.decodeHex())

        @JvmStatic
        fun isValidAddress(address: String): Boolean = try {
            val (hrp, _) = Bech32.decode(address)
            hrp == Bech32Hrp.ACCOUNT
        } catch (e: Exception) {
            false
        }
    }
}

fun TerraWallet(address: String): TerraWallet = TerraWalletImpl(address)

fun TerraWallet(publicKey: ByteArray): TerraWallet = TerraWalletImpl(PublicKey(publicKey))

fun TerraWallet(key: Key): TerraWallet = TerraWalletImpl(key)

class TerraWalletImpl private constructor(override val address: String, override val key: Key?) : TerraWallet {

    constructor(address: String) : this(address, null)

    constructor(key: Key) : this(key.accountAddress, key)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TerraWallet) {
            return false
        }

        return address == other.address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}

val Key.accountAddress: String
    get() {
        val hashed = Ripemd160.hash(SHA_256.hash(this.publicKey))

        return Bech32.encode(Bech32Hrp.ACCOUNT, Bech32.toWords(hashed))
    }

val Key.accountPublicKey: String
    get() {
        val data = BECH32_PUBLIC_KEY_DATA_PREFIX + this.publicKey

        return Bech32.encode(Bech32Hrp.ACCOUNT_PUBLIC_KEY, Bech32.toWords(data))
    }

internal val BECH32_PUBLIC_KEY_DATA_PREFIX = HEX.decode("eb5ae98721")
