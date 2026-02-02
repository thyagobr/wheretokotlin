package com.whereto.app.dtos.events

import com.whereto.app.domain.Event
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val events: List<Event>
)

@Serializable
data class SingleEventResponse(
    val event: Event
)