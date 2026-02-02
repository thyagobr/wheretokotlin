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
                val token = service.authenticate(request.email, request.password)

                if (token != null) {
                    call.respond(HttpStatusCode.OK, ApiResponse(LoginResponse(token)))
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
        }
    }
}