package com.whereto.app.dtos.auth

import com.whereto.app.domain.Role
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val role: String,
    val userId: Int
)