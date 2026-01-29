package com.whereto.app.dtos

import com.whereto.app.domain.Tag
import kotlinx.serialization.Serializable

@Serializable
data class TagResponse(
    val id: Int,
    val text: String
)

fun Tag.toTagResponse(): TagResponse {
    return TagResponse(
        id = id ?: throw IllegalStateException("ID must not be null"),
        text = text
    )
}