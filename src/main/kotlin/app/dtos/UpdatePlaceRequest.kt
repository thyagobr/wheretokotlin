package com.whereto.app.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaceRequest(
    val name: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null
)
