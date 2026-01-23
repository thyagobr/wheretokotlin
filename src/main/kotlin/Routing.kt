package com.whereto

import com.whereto.app.controllers.EventController
import com.whereto.app.controllers.PlaceController
import com.whereto.app.controllers.SessionController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val eventController: EventController by inject<EventController>()
    val placeController: PlaceController by inject<PlaceController>()
    val sessionController: SessionController by inject<SessionController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        eventController.registerRoutes(this)
        placeController.registerRoutes(this)
        sessionController.registerRoutes(this)
    }
}
