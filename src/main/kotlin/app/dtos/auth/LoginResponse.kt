package com.whereto.app.dtos.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
)