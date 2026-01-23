package com.example

import com.example.com.whereto.app.controllers.EventController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import com.example.com.whereto.app.routing.eventRoutes
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
