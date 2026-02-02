package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.domain.Tag
import com.whereto.app.domain.TaggableType
import com.whereto.app.dtos.places.CreatePlaceRequest
import com.whereto.app.dtos.places.UpdatePlaceRequest
import com.whereto.app.repositories.PlaceRepository
import com.whereto.app.repositories.TagRepository
import com.whereto.db.tables.Tags.taggableType
import io.ktor.server.plugins.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PlaceServiceImpl(
    private val repository: PlaceRepository,
    private val tagRepository: TagRepository,
): PlaceService {

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
            val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            val newPlace = repository.create(placeParams)
            placeParams.tags.forEach { tagParam ->
                tagRepository.create(tagParam, newPlace, newPlace.id)
            }
            // Refetch with all tags loaded
            repository.findById(newPlace.id)
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

    override suspend fun deletePlace(id: Int): Boolean {
        val result = withContext(Dispatchers.IO) {
            repository.delete(id)
        }

        return result
    }

    override suspend fun searchAddress(query: String): List<OpenMapsClient.OpenMapResult> {
        val results = withContext(Dispatchers.IO) {
            val client: MapsClient = OpenMapsClient(
                apiKey = "API_KEY",
            )

            client.searchAddress(
                query = query,
            )
        }

        return results
    }
}
