package com.whereto.app.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaceRequest(
    val name: String,
    val address: String,
    val city: String,
    val country: String
)
