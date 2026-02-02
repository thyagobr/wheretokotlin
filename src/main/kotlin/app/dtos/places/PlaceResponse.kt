package com.whereto.app.dtos.places

import com.whereto.app.domain.Place
import com.whereto.app.dtos.tags.TagResponse
import com.whereto.app.dtos.tags.toTagResponse
import kotlinx.serialization.Serializable

/**
 * PlaceResponse - DTO representing a Place without timestamps.
 * Used for API responses to exclude createdAt and updatedAt fields.
 */
@Serializable
data class PlaceResponse(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val tags: List<TagResponse> = listOf(),
)

fun Place.toPlaceResponse(): PlaceResponse {
    return PlaceResponse(
        id = id ?: throw IllegalStateException("Place must have an id to create a response"),
        name = name,
        address = address,
        city = city,
        country = country,
        tags = tags.map { it.toTagResponse() },
    )
}

/**
 * Wrapper for single place response: { place: PlaceResponse }
 */
@Serializable
data class SinglePlaceResponse(
    val place: PlaceResponse
)

/**
 * Wrapper for multiple places response: { places: List<PlaceResponse> }
 */
@Serializable
data class MultiplePlacesResponse(
    val places: List<PlaceResponse>
)
