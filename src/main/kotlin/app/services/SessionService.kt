package com.whereto.app.services

import com.whereto.app.repositories.UserRepository
import com.whereto.utils.PasswordHasher

class SessionService(private val repository: UserRepository) {
    fun authenticate(email: String?, password: String?): String? {
        if (email == null || password == null) {
            return null
        }
        val user = repository.findByEmail(email) ?: return null

        if (PasswordHasher.verify(password, user.passwordDigest)) {
            println("Token found")
            return user.token
        } else {
            println("Token not found")
            return null
        }
    }
}