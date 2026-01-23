package com.whereto.app.domain

import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startsAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endsAt: LocalDateTime?
)
