package com.whereto.app.domain

import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int? = null,
    val taggableId: Int,
    val taggableType: String,
    val text: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = null
)

enum class TaggableType {
    Place,
    Event
}