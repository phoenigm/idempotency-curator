package ru.phoenigm.idempotency.curator.core

import java.time.Duration

data class IdempotentEndpointSettings(
    val url: String,
    val method: String,
    val ttl: Duration?,
    val retryDelay: Duration?,
    val retryCount: Int,
    val errorHttpCode: Int,
    val errorMessage: String
)