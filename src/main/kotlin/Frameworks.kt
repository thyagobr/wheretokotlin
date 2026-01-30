package com.whereto

import com.whereto.app.controllers.EventController
import com.whereto.app.controllers.PlaceController
import com.whereto.app.controllers.SessionController
import com.whereto.app.repositories.EventRepository
import com.whereto.app.repositories.PlaceRepository
import com.whereto.app.repositories.UserRepository
import com.whereto.app.services.EventService
import com.whereto.app.services.PlaceService
import com.whereto.app.services.PlaceServiceImpl
import com.whereto.app.services.SessionService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single { EventRepository() }
            single { EventService(get()) }
            single { EventController(get()) }

            single { PlaceRepository() }
            single<PlaceService> { PlaceServiceImpl(get()) }
            single { PlaceController(get()) }

            single { UserRepository() }
            single { SessionService(get()) }
            single { SessionController(get()) }
        })
    }
}
