package ru.phoenigm.idempotency.curator.core.data.inmemory

import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import java.util.concurrent.ConcurrentHashMap

@Component
open class InMemoryIdempotencyKeyHolder : IdempotencyKeyHolder {
    private val idempotencyKeyMap = ConcurrentHashMap<String, HttpData>()

    override fun put(idempotencyKey: String, httpData: HttpData) {
        idempotencyKeyMap[idempotencyKey] = httpData
    }

    override fun get(idempotencyKey: String): HttpData? {
        return idempotencyKeyMap[idempotencyKey]
    }

    override fun remove(idempotencyKey: String): HttpData? {
        return idempotencyKeyMap.remove(idempotencyKey)
    }
}