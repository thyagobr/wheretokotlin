package com.whereto.app.controllers

import com.whereto.app.dtos.ApiResponse
import com.whereto.app.dtos.auth.LoginRequest
import com.whereto.app.dtos.auth.LoginResponse
import com.whereto.app.services.SessionService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.response.respond
import io.ktor.server.routing.post

class SessionController(private val service: SessionService) {
    fun registerRoutes(route: Route) {
        route.route("/auth/login") {
            post {
                val request = call.receive<LoginRequest>()
                val user = service.authenticate(request.email, request.password) ?: return@post call.respond(HttpStatusCode.Forbidden)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        LoginResponse(
                            token = user.token,
                            role = user.role,
                            userId = user.id
                        )
                    )
                )
            }
        }
    }
}