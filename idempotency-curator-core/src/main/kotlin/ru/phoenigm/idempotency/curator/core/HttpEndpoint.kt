package ru.phoenigm.idempotency.curator.core

data class HttpEndpoint(
    val method: String,
    val url: String
)