package com.whereto.app.controllers

import com.whereto.app.domain.Place
import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.dtos.PlacesResponse
import com.whereto.app.services.PlaceService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class PlaceController(private val service: PlaceService) {
    fun registerRoutes(route: Route) {
        route.route("/places") {
            get {
                call.respond(
                    ApiResponse<PlacesResponse>(
                        data = (PlacesResponse(service.getAllPlaces()))
                    )
                )
            }

            post {
                val placeParams = call.receive< CreatePlaceRequest>()
                val createdPlace = service.createPlace(placeParams)
                println("Place created: $createdPlace")
                call.respond(HttpStatusCode.Created, mapOf("data" to mapOf("place" to createdPlace)))
            }
        }
    }
}