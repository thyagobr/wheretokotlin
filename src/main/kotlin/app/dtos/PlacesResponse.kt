package com.whereto.app.dtos

import com.whereto.app.domain.Place
import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponse(
    val places: List<Place>
){

}
