package com.whereto.app.dtos.ws.chat

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

@Serializable
sealed class ClientMessage {
  @Serializable
  @SerialName("chat_request")
  data class ChatRequest(
    val userId: Int,
    val placeId: Int
  ): ClientMessage()

  @Serializable
  @SerialName("disconnect")
  data class Disconnect(
    val userId: Int
  ): ClientMessage()
}

@Serializable
sealed class ServerMessage {
  @Serializable
  @SerialName("welcome_message")
  data class WelcomeMessage(
    val message: String
  ): ServerMessage()

  @Serializable
  @SerialName("chat_response")
  data class ChatResponse(
    val roomId: Int,
    val message: String
  ): ServerMessage()

  @Serializable
  @SerialName("unknown_message")
  data class UnknownMessage(
    val message: String
  ): ServerMessage()
}
