/*
 * This code was write with reference to {@link kotlinx.serialization.PolymorphicSerializer}
 * and {@link kotlinx.serialization.internal.AbstractPolymorphicSerializer}
 * in org.jetbrains.kotlinx:kotlinx-serialization:1.2.2
 *
 * That codes are Copyright 2017-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package money.terra.proto.serializer

import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
abstract class AbstractPolymorphicSerializer<T : Any>(
    val baseClass: KClass<T>,
) : KSerializer<T> {

    override fun serialize(encoder: Encoder, value: T) {
        val actualSerializer = findPolymorphicSerializer(encoder, value)

        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, actualSerializer.descriptor.serialName)
            encodeSerializableElement(descriptor, 1, actualSerializer, value)
        }
    }

    override fun deserialize(decoder: Decoder): T = decoder.decodeStructure(descriptor) {
        var klassName: String? = null
        var value: Any? = null
        if (decodeSequentially()) {
            return decodeSequentially(this)
        }

        mainLoop@ while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> {
                    break@mainLoop
                }
                0 -> klassName = decodeStringElement(descriptor, index)
                1 -> {
                    klassName = requireNotNull(klassName) { "Cannot read polymorphic value before its type token" }
                    val serializer = findPolymorphicSerializer(decoder, klassName)
                    value = decodeSerializableElement(descriptor, index, serializer)
                }
                else -> throw SerializationException(
                    "Invalid index in polymorphic deserialization of " +
                            (klassName ?: "unknown class") +
                            "\n Expected 0, 1 or DECODE_DONE(-1), but found $index"
                )
            }
        }
        @Suppress("UNCHECKED_CAST")
        requireNotNull(value) { "Polymorphic value has not been read for class $klassName" } as T
    }

    private fun decodeSequentially(compositeDecoder: CompositeDecoder): T {
        val klassName = compositeDecoder.decodeStringElement(descriptor, 0)
        val serializer = findPolymorphicSerializer(compositeDecoder, klassName)

        return compositeDecoder.decodeSerializableElement(descriptor, 1, serializer)
    }

    protected fun findPolymorphicSerializer(
        encoder: Encoder,
        value: T,
    ): SerializationStrategy<T> = encoder.serializersModule.getPolymorphic(baseClass, value)
        ?: throwSubtypeNotRegistered(value::class.simpleName ?: value::class.toString(), baseClass)

    protected fun findPolymorphicSerializer(
        decoder: Decoder,
        className: String?,
    ): DeserializationStrategy<out T> = decoder.serializersModule.getPolymorphic(baseClass, className)
        ?: throwSubtypeNotRegistered(className, baseClass)

    protected fun findPolymorphicSerializer(
        decoder: CompositeDecoder,
        className: String?,
    ): DeserializationStrategy<out T> = decoder.serializersModule.getPolymorphic(baseClass, className)
        ?: throwSubtypeNotRegistered(className, baseClass)

    @JvmName("throwSubtypeNotRegistered")
    protected fun throwSubtypeNotRegistered(subClassName: String?, baseClass: KClass<*>): Nothing {
        val scope = "in the scope of '${baseClass.simpleName}'"
        throw SerializationException(
            if (subClassName == null)
                "Class discriminator was missing and no default polymorphic serializers were registered $scope"
            else
                "Class '$subClassName' is not registered for polymorphic serialization $scope.\n" +
                        "Mark the base class as 'sealed' or register the serializer explicitly."
        )
    }
}