package ru.phoenigm.idempotency.curator.core

interface IdempotencyKeyHolder {

    fun put(httpData: HttpData, idempotencyKey: String)

    fun get(httpData: HttpData): String?

    fun remove(httpData: HttpData): String?

    fun clear();
}