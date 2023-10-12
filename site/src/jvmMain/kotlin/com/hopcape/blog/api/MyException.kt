package com.hopcape.blog.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MyException(val message: String)

object MyExceptionSerializer : KSerializer<MyException> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MyException", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: MyException) {
        encoder.encodeString(value.message)
    }

    override fun deserialize(decoder: Decoder): MyException {
        val message = decoder.decodeString()
        return MyException(message)
    }
}
