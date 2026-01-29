package com.whereto.app.controllers

import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.dtos.MultiplePlacesResponse
import com.whereto.app.dtos.SinglePlaceResponse
import com.whereto.app.dtos.UpdatePlaceRequest
import com.whereto.app.dtos.toPlaceResponse
import com.whereto.app.services.PlaceService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

class PlaceController(private val service: PlaceService) {
    fun registerRoutes(route: Route) {
        route.route("/places") {
            get {
                val places = service.getAllPlaces()
                val placeResponses = places.map { it.toPlaceResponse() }
                call.respond(
                    ApiResponse(
                        data = MultiplePlacesResponse(places = placeResponses)
                    )
                )
            }

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val place = service.getPlaceById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(
                    ApiResponse(
                        data = SinglePlaceResponse(place.toPlaceResponse())
                    )
                )
            }

            post {
                val placeParams = call.receive<CreatePlaceRequest>()
                val createdPlace = service.createPlace(placeParams)
                val placeResponse = createdPlace.toPlaceResponse()
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        data = SinglePlaceResponse(place = placeResponse)
                    )
                )
            }

            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val placeParams = call.receive<UpdatePlaceRequest>()
                val place = service.updatePlace(id, placeParams)
                call.respond(
                    ApiResponse(
                        data = SinglePlaceResponse(place = place.toPlaceResponse())
                    )
                )
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                when (service.deletePlace(id)) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}