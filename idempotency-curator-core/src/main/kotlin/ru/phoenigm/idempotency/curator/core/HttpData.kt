package ru.phoenigm.idempotency.curator.core

import java.io.Serializable
import java.time.Duration

data class HttpData(
    val url: String,
    val method: String
) : Serializable