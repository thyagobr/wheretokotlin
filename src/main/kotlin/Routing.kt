package com.whereto

import com.whereto.app.controllers.EventController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val eventController: EventController by inject<EventController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        //eventRoutes()
        eventController.registerRoutes(this)
    }
}
