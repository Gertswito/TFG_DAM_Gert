package com.gert.tfgdam.core.network

data class ApiError(
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val message: String?,
    val path: String?
)