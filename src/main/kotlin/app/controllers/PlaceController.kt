package com.whereto.app.controllers

import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.places.CreatePlaceRequest
import com.whereto.app.dtos.places.MultiplePlacesResponse
import com.whereto.app.dtos.places.SinglePlaceResponse
import com.whereto.app.dtos.places.UpdatePlaceRequest
import com.whereto.app.dtos.places.toPlaceResponse
import com.whereto.app.services.OpenMapsClient
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
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.patch

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

            // Protected routes: require Authorization: Bearer <token> header
            authenticate("bearer") {
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

                patch("{id}") {
                    println("We're in patch")
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
                    println("ID is $id")
                    val placeParams = call.receive<UpdatePlaceRequest>()
                    println("Place params: $placeParams")
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

                get("search_address") {
                    val name = call.request.queryParameters["name"]
                    val city = call.request.queryParameters["city"]
                    val country = call.request.queryParameters["country"]
                    val query = "$name $city, $country"
                    val addresses = service.searchAddress(query)
                    call.respond(
                        ApiResponse(
                            data = addresses
                        )
                    )
                }
            }
        }
    }
}