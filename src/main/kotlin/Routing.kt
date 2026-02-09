package com.whereto

import com.whereto.app.controllers.EventController
import com.whereto.app.controllers.PlaceController
import com.whereto.app.controllers.SessionController
import com.whereto.app.controllers.WebsocketController
import com.whereto.app.dtos.ws.chat.ClientMessage
import com.whereto.app.dtos.ws.chat.ServerMessage
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.readText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
  val eventController: EventController by inject<EventController>()
  val placeController: PlaceController by inject<PlaceController>()
  val sessionController: SessionController by inject<SessionController>()
  val websocketController: WebsocketController by inject<WebsocketController>()

  routing {
    eventController.registerRoutes(this)
    placeController.registerRoutes(this)
    sessionController.registerRoutes(this)
    websocketController.registerRoutes(this)
  }
}
