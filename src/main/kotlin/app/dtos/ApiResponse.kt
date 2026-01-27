package com.whereto.app.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T
)