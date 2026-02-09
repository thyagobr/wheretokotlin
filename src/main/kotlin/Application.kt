package com.whereto

import com.whereto.app.repositories.UserRepository
import com.whereto.db.DatabaseFactory
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.websocket.WebSockets
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS
import org.koin.ktor.ext.inject
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.request.receiveText

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    intercept(ApplicationCallPipeline.Monitoring) {
        val bodyText = call.receiveText()
        call.application.log.info("Incoming request body: $bodyText")
        proceed() // must call this so the route still runs
    }

    install(DoubleReceive)

    install(CORS) {
        anyHost()

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
    }

    DatabaseFactory.init(environment.config)
    configureFrameworks()
    configureSerialization()
    val userRepository: UserRepository by inject()
    configureAuthentication(userRepository)
    install(WebSockets)
    configureRouting()

    install(CallLogging)
}
