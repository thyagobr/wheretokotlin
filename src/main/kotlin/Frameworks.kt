package com.whereto

import com.whereto.app.controllers.EventController
import com.whereto.app.controllers.PlaceController
import com.whereto.app.repositories.EventRepository
import com.whereto.app.repositories.PlaceRespository
import com.whereto.app.services.EventService
import com.whereto.app.services.PlaceService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single<HelloService> {
                HelloService {
                    println(environment.log.info("Hello, World!"))
                }
            }

            single { EventRepository() }
            single { EventService(get()) }
            single { EventController(get()) }

            single { PlaceRespository() }
            single { PlaceService(get()) }
            single { PlaceController(get()) }
        })
    }
}
