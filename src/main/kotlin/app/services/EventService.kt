package com.whereto.app.services

import com.whereto.app.domain.Event
import com.whereto.app.repositories.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventService(private val repository: EventRepository) {
    suspend fun getAllEventsForPlace(placeId: Int): List<Event> =
        withContext(Dispatchers.IO) {
            repository.findAllByPlaceId(placeId)
        }

    suspend fun getAllEvents(): List<Event> =
        withContext(Dispatchers.IO) {
            repository.findAll()
        }

    suspend fun getEvent(id: Int): Event? =
        withContext(Dispatchers.IO) {
            repository.findById(id)
        }
}