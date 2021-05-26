package ru.phoenigm.idempotency.curator.core.data

import ru.phoenigm.idempotency.curator.core.HttpData

interface IdempotencyKeyHolder {

    fun put(idempotencyKey: String, httpData: HttpData)

    fun get(idempotencyKey: String): HttpData?

    fun remove(idempotencyKey: String): HttpData?
}
