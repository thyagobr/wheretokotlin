package com.whereto.app.services

interface MapsClient {
    suspend fun searchAddress(query: String): List<OpenMapsClient.OpenMapResult>
}