package com.whereto.app.controllers

import com.whereto.app.dtos.ws.chat.ClientMessage
import com.whereto.app.dtos.ws.chat.ServerMessage
import io.ktor.server.websocket.*
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.readText

class WebsocketController {
  fun registerRoutes(route: io.ktor.server.routing.Route) {
    route.webSocket("/ws") {
      handleWebSocket(this)
    }
  }

  private suspend fun handleWebSocket(session: io.ktor.server.websocket.DefaultWebSocketServerSession) {
    val json = kotlinx.serialization.json.Json {
      ignoreUnknownKeys = true
    }

    val welcomeMessage = ServerMessage.WelcomeMessage(
      message = "Welcome to the WebSocket server!"
    )
    session.send(json.encodeToString(welcomeMessage))

    for (frame in session.incoming) {
      frame as? Frame.Text ?: continue
      val payload = json.decodeFromString<ClientMessage>(frame.readText())
      when (payload) {
        is ClientMessage.Disconnect -> {
          session.close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
        }
        is ClientMessage.ChatRequest -> {
          val chatResponse = ServerMessage.ChatResponse(
            roomId = payload.placeId, // Assuming placeId is used as roomId
            message = "User ${payload.userId} joined the chat for place ${payload.placeId}"
          )
          session.send(json.encodeToString(chatResponse))
        }
        else -> {
          val response = ServerMessage.UnknownMessage(
            message = "Unknown message type: ${payload::class.simpleName}"
          )
          session.send(json.encodeToString(response))
        }
      }
    }
  }
}
