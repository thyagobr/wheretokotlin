package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.dtos.UpdatePlaceRequest
import com.whereto.app.repositories.PlaceRepository
import io.ktor.server.plugins.NotFoundException
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

    override suspend fun updatePlace(id: Int, placeParams: UpdatePlaceRequest): Place {
        val place = withContext(Dispatchers.IO) {
            val existingPlace = repository.findById(id) ?: throw NotFoundException("Place not found")
            val updatedPlace = existingPlace.copy(
                name = placeParams.name ?: existingPlace.name,
                address = placeParams.address ?: existingPlace.address,
                city = placeParams.city ?: existingPlace.city,
                country = placeParams.country ?: existingPlace.country
            )
            repository.update(updatedPlace)
        }

        return place
    }
}
