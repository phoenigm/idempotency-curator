package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder

@Component
open class IdempotencyKeyProcessor(
    private val idempotencyKeyHolder: IdempotencyKeyHolder,
    private val idempotencyKeyConfig: IdempotencyKeyConfig
) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    fun process(idempotencyKey: String, httpData: HttpData): Boolean {
        logger.info { "Start processing idempotency key = $idempotencyKey, http data = $httpData" }
        val retryCount = idempotencyKeyConfig.retryCount

        repeat(retryCount) {
            if (isLockAcquired(idempotencyKey)) {
                logger.info { "Locked for idempotency key: $idempotencyKey, http data: $httpData" }
            } else {
                idempotencyKeyHolder.put(idempotencyKey, httpData)
                return true
            }
        }
        return false
    }

    fun releaseLock(idempotencyKey: String) {
        logger.info { "Lock released for idempotency key: $idempotencyKey" }
        idempotencyKeyHolder.remove(idempotencyKey)
    }

    private fun isLockAcquired(idempotencyKey: String): Boolean {
        return idempotencyKeyHolder.get(idempotencyKey) != null
    }
}