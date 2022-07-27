package money.terra.client.grpc.util

import com.google.protobuf.kotlin.unpack
import money.terra.model.PublicKey
import money.terra.type.Binary

fun com.google.protobuf.Any.unpackPublicKey(): PublicKey = when (typeUrl) {
    "/cosmos.crypto.secp256k1.PubKey" -> PublicKey.Secp256k1(Binary(unpack<cosmos.crypto.secp256k1.Keys.PubKey>().key.toByteArray()))
    "/cosmos.crypto.ed25519.PubKey" -> PublicKey.Ed25519(Binary(unpack<cosmos.crypto.ed25519.Keys.PubKey>().key.toByteArray()))
    else -> throw IllegalArgumentException("Invalid type : $typeUrl")
}