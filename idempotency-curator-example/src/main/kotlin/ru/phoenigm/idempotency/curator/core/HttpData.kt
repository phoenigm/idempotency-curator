package ru.phoenigm.idempotency.curator.core

data class HttpData(
    val url: String,
    val method: String
)