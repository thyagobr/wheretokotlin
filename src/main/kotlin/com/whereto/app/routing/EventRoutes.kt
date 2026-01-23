package com.example.com.whereto.app.routing

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.eventRoutes() {
    route("/events") {
        get {
            call.respondText("Event list")
        }
    }
}