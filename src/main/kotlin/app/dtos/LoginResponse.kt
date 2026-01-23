package com.whereto.app.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
)