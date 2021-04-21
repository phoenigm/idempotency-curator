package ru.phoenigm.idempotency.curator.core

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
open class IdempotencyKeyHolderImpl : IdempotencyKeyHolder {
    private val idempotencyKeyMap = ConcurrentHashMap<HttpData, String>()

    override fun put(httpData: HttpData, idempotencyKey: String) {
        println("Now by $httpData = ${idempotencyKeyMap[httpData]}" )
        idempotencyKeyMap[httpData] = idempotencyKey
    }

    override fun get(httpData: HttpData): String? {
        return idempotencyKeyMap[httpData]
    }

    override fun remove(httpData: HttpData): String? {
        return idempotencyKeyMap.remove(httpData)
    }

    override fun clear() {
        idempotencyKeyMap.clear();
    }
}