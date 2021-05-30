package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder

@Component
open class IdempotencyKeyProcessor(
    private val idempotencyKeyHolder: IdempotencyKeyHolder,
) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    fun process(idempotencyKey: String, endpointSettings: IdempotentEndpointSettings): IdempotencyProcessingStatus {
        logger.info { "Start processing idempotency key = $idempotencyKey, endpoint settings = $endpointSettings" }
        if (isLockTtlSpecified(endpointSettings) && !isLockAcquired(idempotencyKey)) {
            logger.info { "Ttl specified for idempotency key = $idempotencyKey" }
            serveIdempotencyKey(idempotencyKey, endpointSettings)
            return IdempotencyProcessingStatus.TTL_SPECIFIED
        }

        repeat(endpointSettings.retryCount) { retry ->
            if (isLockAcquired(idempotencyKey)) {
                logger.info { "Locked for idempotency key: $idempotencyKey, endpoint settings = $endpointSettings" }
                endpointSettings.retryDelay?.also {
                    logger.info { "Retry number ${retry + 1}. Time ms: ${it.toMillis()}" }
                    Thread.sleep(it.toMillis())
                }
            } else {
                serveIdempotencyKey(idempotencyKey, endpointSettings)
                return IdempotencyProcessingStatus.FREE
            }
        }
        return IdempotencyProcessingStatus.LOCKED
    }

    private fun serveIdempotencyKey(idempotencyKey: String, endpointSettings: IdempotentEndpointSettings) {
        idempotencyKeyHolder.put(
            idempotencyKey,
            HttpData(endpointSettings.url, endpointSettings.method),
            endpointSettings.ttl
        )
    }

    private fun isLockTtlSpecified(endpointSettings: IdempotentEndpointSettings) = endpointSettings.ttl != null

    fun releaseLock(idempotencyKey: String) {
        logger.info { "Lock released for idempotency key: $idempotencyKey" }
        idempotencyKeyHolder.remove(idempotencyKey)
    }

    private fun isLockAcquired(idempotencyKey: String): Boolean {
        return idempotencyKeyHolder.get(idempotencyKey) != null
    }
}