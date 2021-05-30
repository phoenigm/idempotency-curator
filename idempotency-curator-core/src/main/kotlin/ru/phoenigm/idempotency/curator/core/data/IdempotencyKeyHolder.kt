package ru.phoenigm.idempotency.curator.core.data

import ru.phoenigm.idempotency.curator.core.HttpData
import java.time.Duration

interface IdempotencyKeyHolder {

    fun put(idempotencyKey: String, httpData: HttpData, ttl: Duration? = null)

    fun get(idempotencyKey: String): HttpData?

    fun remove(idempotencyKey: String): HttpData?
}
