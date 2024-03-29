package ru.phoenigm.idempotency.curator.core

import java.time.Duration

data class IdempotencyKeyConfig(
    val lockTtl: Duration?,
    val retryCount: Int,
    val retryDelay: Duration,
    val header: String,
    val errorMessage: String,
    val errorHttpCode: Int
)