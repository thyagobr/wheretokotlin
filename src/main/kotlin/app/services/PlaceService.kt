package com.whereto.app.services

import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.dtos.UpdatePlaceRequest

interface PlaceService {
    suspend fun getAllPlaces(): List<Place>
    suspend fun getPlaceById(id: Int): Place?
    suspend fun createPlace(placeParams: CreatePlaceRequest): Place
    suspend fun updatePlace(id: Int, placeParams: UpdatePlaceRequest): Place
    suspend fun deletePlace(id: Int): Boolean
}
