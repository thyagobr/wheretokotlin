package com.whereto.app.domain

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val passwordDigest: String,
    val role: String?,
    val token: String?
)
