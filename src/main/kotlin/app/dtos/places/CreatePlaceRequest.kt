package com.whereto.app.dtos.places

import com.whereto.app.dtos.tags.TagRequest
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaceRequest(
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val tags: List<TagRequest>
)