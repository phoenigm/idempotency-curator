package ru.phoenigm.idempotency.curator.core

import org.springframework.stereotype.Component

@Component
open class IdempotencyKeyProcessor(
    private val idempotencyKeyHolder: IdempotencyKeyHolder,
    private val idempotencyKeyConfig: IdempotencyKeyConfig
) {

    fun process(httpData: HttpData, idempotencyKey: String): Boolean {
        println("process $httpData $idempotencyKey")
        val retryCount = idempotencyKeyConfig.retryCount

        repeat(retryCount) {
            if (isLockAcquired(httpData)) {
                println("Suka, locked $httpData")
            } else {
                idempotencyKeyHolder.put(httpData, idempotencyKey)
                return true
            }
        }
        return false
    }

    fun releaseLocks() {
        println("release all locks")
        idempotencyKeyHolder.clear()
    }

    fun releaseLock(httpData: HttpData) {
        println("release lock $httpData")
        idempotencyKeyHolder.remove(httpData)
    }

    fun isLockAcquired(httpData: HttpData): Boolean {
        return idempotencyKeyHolder.get(httpData) != null
    }
}