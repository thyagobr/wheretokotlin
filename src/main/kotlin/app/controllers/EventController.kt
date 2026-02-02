package com.whereto.app.controllers

import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.EventResponse
import com.whereto.app.dtos.SingleEventResponse
import com.whereto.app.dtos.UserPrincipal
import com.whereto.app.dtos.events.CreateEventRequest
import com.whereto.app.dtos.events.UpdateEventRequest
import com.whereto.app.services.EventService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class EventController(private val service: EventService) {
    fun registerRoutes(route: Route) {
        route.route("/places/{id}/events") {
            get {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)

                call.respond(
                    ApiResponse(
                        data = EventResponse(service.getAllEventsForPlace(id))
                    )
                )
            }

            authenticate ("bearer") {
                post {
                    println("In the route")
                    val eventParams = call.receive<CreateEventRequest>()
                    println(eventParams)
                    val event = service.create(
                        eventParams.copy(userId = call.principal<UserPrincipal>()?.userId)
                    )
                    if (event != null) {
                        call.respond(
                            ApiResponse(
                                data = SingleEventResponse(event)
                            )
                        )
                    }
                }
            }
        }

        route.route("/events") {
            get {
                call.respond(
                    ApiResponse(
                        data = EventResponse(service.getAllEvents())
                    )
                )
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val event = service.getEvent(id)
                if (event != null) {
                    call.respond(
                        ApiResponse(
                            data = SingleEventResponse(event)
                        )
                    )
                } else {
                    call.respondText("Event not found", status = HttpStatusCode.NotFound)
                }
            }

            patch("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
                val eventParams = call.receive<UpdateEventRequest>()
                val event = service.update(id, eventParams)
                if (event != null) {
                    call.respond(
                        ApiResponse(
                            data = SingleEventResponse(event)
                        )
                    )
                } else {
                    call.respondText("Event not found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}