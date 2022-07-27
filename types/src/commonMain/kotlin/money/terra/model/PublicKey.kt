package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import money.terra.type.Binary

sealed class PublicKey(@Contextual val value: Binary) {
    class Secp256k1(value: Binary) : PublicKey(value)

    class Ed25519(value: Binary) : PublicKey(value)
}
