package money.terra.wallet.test

import money.terra.key.RawKey
import money.terra.wallet.TerraWallet
import money.terra.wallet.accountPublicKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TerraWalletTest {

    @Test
    fun succeedValidAddress() {
        assertTrue {
            TerraWallet.isValidAddress("terra1ra2flyqkklt75z43j2030n08h2ctgqnfalzngy")
        }
    }

    @Test
    fun failedInvalidAddress() {
        assertFalse { TerraWallet.isValidAddress("terra1ra2flyqkklt75z43j2030n08h2ctgqnfalzng") }
        assertFalse { TerraWallet.isValidAddress("erra1ra2flyqkklt75z43j2030n08h2ctgqnfalzngy") }
        assertFalse { TerraWallet.isValidAddress("terrapub1ra2flyqkklt75z43j2030n08h2ctgqnfalzngy") }
        assertFalse { TerraWallet.isValidAddress("terrara2flyqkklt75z43j2030n08h2ctgqnfalzngy") }
        assertFalse { TerraWallet.isValidAddress("terra1") }
        assertFalse { TerraWallet.isValidAddress("1ra2flyqkklt75z43j2030n08h2ctgqnfalzngy") }
        assertFalse { TerraWallet.isValidAddress("terra1r") }
        assertFalse { TerraWallet.isValidAddress("tea1r") }
        assertFalse { TerraWallet.isValidAddress("tear") }
    }

    @Test
    fun succeedFromKeyPair() {
        val wallet1 = TerraWallet.fromRawKey(NORMAL.privateKeyHex, NORMAL.publicKeyHex)
        assertEquals(NORMAL.address, wallet1.address)
        assertEquals(NORMAL.privateKeyHex, (wallet1.key as RawKey).privateKeyHex)
        assertEquals(NORMAL.publicKeyHex, (wallet1.key as RawKey).publicKeyHex)
        assertEquals(NORMAL.publicKeyBech32, wallet1.key!!.accountPublicKey)

        val wallet2 = TerraWallet.fromRawKey(ZERO_END_PRIVATE_KEY.privateKeyHex, ZERO_END_PRIVATE_KEY.publicKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.address, wallet2.address)
        assertEquals(ZERO_END_PRIVATE_KEY.privateKeyHex, (wallet2.key as RawKey).privateKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyHex, (wallet2.key as RawKey).publicKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyBech32, wallet2.key!!.accountPublicKey)
    }

    @Test
    fun succeedFromPrivateKey() {
        val wallet = TerraWallet.fromRawKey(NORMAL.privateKeyHex)
        assertEquals(NORMAL.address, wallet.address)
        assertEquals(NORMAL.privateKeyHex, (wallet.key as RawKey).privateKeyHex)
        assertEquals(NORMAL.publicKeyHex, (wallet.key as RawKey).publicKeyHex)
        assertEquals(NORMAL.publicKeyBech32, wallet.key!!.accountPublicKey)

        val wallet2 = TerraWallet.fromRawKey(ZERO_END_PRIVATE_KEY.privateKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.address, wallet2.address)
        assertEquals(ZERO_END_PRIVATE_KEY.privateKeyHex, (wallet2.key as RawKey).privateKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyHex, (wallet2.key as RawKey).publicKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyBech32, wallet2.key!!.accountPublicKey)

        val wallet3 = TerraWallet.fromRawKey(ZERO_TRIM_PRIVATE_KEY.privateKeyHex)
        assertEquals(ZERO_TRIM_PRIVATE_KEY.address, wallet3.address)
        assertEquals("00" + ZERO_TRIM_PRIVATE_KEY.privateKeyHex, (wallet3.key as RawKey).privateKeyHex)
        assertEquals(ZERO_TRIM_PRIVATE_KEY.publicKeyHex, (wallet3.key as RawKey).publicKeyHex)
        assertEquals(ZERO_TRIM_PRIVATE_KEY.publicKeyBech32, wallet3.key!!.accountPublicKey)
    }

    @Test
    fun succeedFromMnemonic() {
        val wallet = TerraWallet.fromMnemonic(NORMAL.mnemonic)
        assertEquals(NORMAL.address, wallet.address)
        assertEquals(NORMAL.privateKeyHex, (wallet.key as RawKey).privateKeyHex)
        assertEquals(NORMAL.publicKeyHex, (wallet.key as RawKey).publicKeyHex)
        assertEquals(NORMAL.publicKeyBech32, wallet.key!!.accountPublicKey)

        val wallet2 = TerraWallet.fromMnemonic(ZERO_END_PRIVATE_KEY.mnemonic)
        assertEquals(ZERO_END_PRIVATE_KEY.address, wallet2.address)
        assertEquals(ZERO_END_PRIVATE_KEY.privateKeyHex, (wallet2.key as RawKey).privateKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyHex, (wallet2.key as RawKey).publicKeyHex)
        assertEquals(ZERO_END_PRIVATE_KEY.publicKeyBech32, wallet2.key!!.accountPublicKey)
    }
}