package com.whereto.app.domain

import com.whereto.app.dtos.auth.UserPrincipal
import com.whereto.utils.PasswordHasher

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val passwordDigest: String,
    val role: String = Role.USER.name,
    val token: String
)

enum class Role {
    USER, ADMIN
}

fun User.toUserPrincipal(): UserPrincipal {
    return UserPrincipal(
        userId = id,
        token = token,
        role = role
    )
}