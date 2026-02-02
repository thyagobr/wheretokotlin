package com.whereto.app.dtos.events

import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    val name: String,
    val placeId: Int,
    val description: String,
    val link: String? = null,
    val public: Boolean = false,
    val userId: Int? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startsAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endsAt: LocalDateTime?
)
