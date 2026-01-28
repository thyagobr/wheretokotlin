package com.whereto

import com.whereto.app.controllers.PlaceController
import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.app.services.PlaceService
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.Clock

class FakePlaceService : PlaceService {

    val places = mutableListOf<Place>(
        Place(
            id = 1,
            name = "Cafe",
            address = "Main St",
            city = "Berlin",
            country = "DE",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        ),
        Place(
            id = 2,
            name = "Park",
            address = "Green Ave",
            city = "Berlin",
            country = "DE",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    )

    override suspend fun getAllPlaces(): List<Place> = places

    override suspend fun createPlace(placeParams: CreatePlaceRequest): Place {
        val place = Place(
            id = places.size,
            name = placeParams.name,
            address = placeParams.address,
            city = placeParams.city,
            country = placeParams.country,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        places.add(place)
        return place
    }
}

class PlaceControllerTest {
    @Test
    fun `GET places returns list of places`() = testApplication {
        val fakeService = FakePlaceService()
        val controller = PlaceController(fakeService)

        application {
            configureSerialization()

            routing {
                controller.registerRoutes(this)
            }
        }

        val response = client.get("/places")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Cafe"))
        println(response.bodyAsText())
    }

    @Test
    fun `POST places creates a new place` () = testApplication {
        val fakeService = FakePlaceService()
        val controller = PlaceController(fakeService)

        application {
            configureSerialization()

            routing {
                controller.registerRoutes(this)
            }
        }

        // Configure client with ContentNegotiation for JSON serialization
        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val placeToCreate = CreatePlaceRequest(
            name = "Space Medduza",
            address = "Skalitzer Strasse 17",
            city = "Berlin",
            country = "DE"
        )

        val response = testClient.post("/places") {
            contentType(ContentType.Application.Json)
            setBody(placeToCreate)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Space Medduza"))
        println(response.bodyAsText())
    }
}
