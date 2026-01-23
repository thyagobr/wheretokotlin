package com.example.com.whereto.app.domain

import com.example.utils.LocalDateTimeSerializer
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
