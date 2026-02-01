package com.whereto.app.domain

import com.whereto.app.dtos.PlaceResponse
import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int? = null,
    val name: String,
    val place: Place? = null,
    val description: String,
    val link: String?,
    val public: Boolean,
    val userId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startsAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endsAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = null
)