package com.whereto.app.controllers

import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.EventResponse
import com.whereto.app.services.EventService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

class EventController(private val service: EventService) {
    fun registerRoutes(route: Route) {
        route.route("/events") {
            get {
                call.respond(
                    ApiResponse(
                        data = EventResponse(service.getAllEvents())
                    )
                )
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val event = id?.let { service.getEvent(it) }
                if (event != null) {
                    call.respond(event)
                } else {
                    call.respondText("Event not found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}