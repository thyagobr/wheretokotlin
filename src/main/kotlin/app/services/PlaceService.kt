package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.repositories.PlaceRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceService(private val repository: PlaceRespository) {

    suspend fun getAllPlaces(): List<Place> =
        withContext(Dispatchers.IO) {
            repository.findAll()
        }
}