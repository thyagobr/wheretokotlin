package com.whereto.app.dtos.events

import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEventRequest(
    val name: String?,
    val description: String?,
    //val link: String?,
    val public: Boolean?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startsAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endsAt: LocalDateTime?
)
