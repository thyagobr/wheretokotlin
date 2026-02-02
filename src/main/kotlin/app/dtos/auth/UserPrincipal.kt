package com.whereto.app.dtos.auth

data class UserPrincipal(
    val userId: Int,
    val token: String
)