package com.whereto.app.controllers

import com.whereto.app.services.PlaceService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

class PlaceController(private val service: PlaceService) {
    fun registerRoutes(route: Route) {
        route.route("/places") {
            get {
                call.respond(service.getAllPlaces())
            }
        }
    }
}