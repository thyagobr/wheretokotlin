package com.whereto

import com.whereto.app.dtos.auth.UserPrincipal
import com.whereto.app.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer

fun Application.configureAuthentication(userRepository: UserRepository) {
    install(Authentication) {
        bearer("bearer") {
            authenticate { credentials ->
                println("Authenticating for $credentials")
                val token = credentials.token

                val user = userRepository.findByToken(token)
                    ?: return@authenticate null

                UserPrincipal(
                    userId = user.id,
                    token = token
                )
            }
        }
    }
}
