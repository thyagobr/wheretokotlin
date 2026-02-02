package com.whereto.app.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OpenMapsClient(
    private val apiKey: String,
    private val httpClient: HttpClient = defaultClient()
): MapsClient {
    companion object {
        fun defaultClient() = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Serializable
    data class OpenMapResult(
        val display_name: String,
    )

    override suspend fun searchAddress(query: String): List<OpenMapResult> {
        val baseUrl = "https://nominatim.openstreetmap.org"
        println("Issuing GET to $baseUrl/search with query: $query")
        val result: List<OpenMapResult> = httpClient.get("$baseUrl/search") {
            parameter("q", query)
            parameter("format", "json")
            parameter("addressdetails", 1)
            parameter("limit", 5)
            header("User-Agent", "whereTo/0.1") // required by OpenStreetMap
            // parameter("apikey", apiKey)
        }.body()

        return result.map { OpenMapResult(it.display_name) }
    }
}