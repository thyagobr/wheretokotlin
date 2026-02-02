package com.whereto

import com.whereto.app.controllers.PlaceController
import com.whereto.app.domain.Place
import com.whereto.app.dtos.places.CreatePlaceRequest
import com.whereto.app.dtos.places.UpdatePlaceRequest
import com.whereto.app.services.PlaceService
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    override suspend fun getPlaceById(id: Int): Place? = places.find { it.id == id }

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

    override suspend fun updatePlace(id: Int, placeParams: UpdatePlaceRequest): Place {
        val place = places.find { it.id == id }!!
        val index = places.indexOfFirst { it.id == place.id }
        val newPlace = place.copy(
            name = placeParams.name ?: place.name,
            address = placeParams.address ?: place.address,
            city = placeParams.city ?: place.city,
            country = placeParams.country ?: place.country,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        places[index] = newPlace
        return newPlace
    }

    override suspend fun deletePlace(id: Int): Boolean {
        return places.removeIf { it.id == id }
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
    fun `GET places by id returns correct place`() = testApplication {
        val fakeService = FakePlaceService()
        val controller = PlaceController(fakeService)

        application {
            configureSerialization()

            routing {
                controller.registerRoutes(this)
            }
        }

        val response = client.get("/places/2")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Park"))
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

    @Test
    fun `PUT place creates a new place`() = testApplication {
        val fakeService = FakePlaceService()
        val controller = PlaceController(fakeService)

        application {
            configureSerialization()

            routing {
                controller.registerRoutes(this)
            }
        }

        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val placeToUpdate = UpdatePlaceRequest(
            name = "Space Meduza"
        )

        val response = testClient.put("/places/1") {
            contentType(ContentType.Application.Json)
            setBody(placeToUpdate)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Space Meduza"))
        assertTrue(response.bodyAsText().contains("Main St"))
    }

    @Test
    fun `DELETE place deletes a place`() = testApplication {
        val fakeService = FakePlaceService()
        val controller = PlaceController(fakeService)

        application {
            configureSerialization()

            routing {
                controller.registerRoutes(this)
            }
        }

        val checkExistingResponse = client.get("/places/1")
        assertEquals(HttpStatusCode.OK, checkExistingResponse.status)
        assertTrue(checkExistingResponse.bodyAsText().contains("Cafe"))

        val response = client.delete("/places/1")

        assertEquals(HttpStatusCode.OK, response.status)

        val confirmingResponse = client.get("/places/1")
        assertEquals(HttpStatusCode.NotFound, confirmingResponse.status)
    }
}
