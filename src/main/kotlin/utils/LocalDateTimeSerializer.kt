package com.whereto.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.datetime.LocalDateTime

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        // Encode as ISO-8601 string
        encoder.encodeString(value.toString()) // kotlinx.datetime LocalDateTime to ISO string
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        // Decode from ISO-8601 string
        return LocalDateTime.parse(decoder.decodeString())
    }
}