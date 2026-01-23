package com.whereto.app.domain

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: Int,
    val name: String,
    val country: String,
    val city: String
)