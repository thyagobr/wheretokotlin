package com.whereto.app.dtos

import com.whereto.app.domain.Event
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val events: List<Event>
)
