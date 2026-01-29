package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.repositories.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceServiceImpl(private val repository: PlaceRepository): PlaceService {

    override suspend fun getAllPlaces(): List<Place> =
        withContext(Dispatchers.IO) {
            repository.findAll()
        }

    override suspend fun getPlaceById(id: Int): Place? {

        val place = withContext(Dispatchers.IO) {
            repository.findById(id)
        }

        return place
    }

    override suspend fun createPlace(placeParams: CreatePlaceRequest): Place {
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
