package ru.phoenigm.idempotency.curator.core

import java.io.Serializable

data class HttpData(
    val url: String,
    val method: String
) : Serializable