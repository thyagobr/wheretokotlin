package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.repositories.PlaceRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceService(private val repository: PlaceRespository) {

    suspend fun getAllPlaces(): List<Place> =
        withContext(Dispatchers.IO) {
            repository.findAll()
        }

    suspend fun createPlace(placeParams: CreatePlaceRequest): Place {
        val createdPlace = withContext(Dispatchers.IO) {
            val place = Place(
                name = placeParams.name,
                address = placeParams.address,
                city = placeParams.city,
                country = placeParams.country,
            )
            repository.create(place)
        }

        return createdPlace
    }
}
