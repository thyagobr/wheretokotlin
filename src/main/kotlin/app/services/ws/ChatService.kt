package com.whereto.app.services.ws

class ChatService {
    val rooms = mutableMapOf<Int, MutableSet<Int>>()

    fun addUserToRoom(roomId: Int, userId: Int) {
        rooms[roomId]?.add(userId)
    }
}