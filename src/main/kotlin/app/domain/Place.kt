package com.whereto.app.domain

import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: Int? = null,
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val tags: List<String> = listOf(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = null
)
