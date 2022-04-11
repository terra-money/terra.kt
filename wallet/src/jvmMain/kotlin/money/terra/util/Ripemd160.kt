package money.terra.util

import money.terra.library.ripemd160.Ripemd160

actual object Ripemd160 {

    actual fun hash(data: ByteArray): ByteArray = Ripemd160.getHash(data)
}