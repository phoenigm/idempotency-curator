package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder

@Component
open class IdempotencyKeyProcessor(
    private val idempotencyKeyHolder: IdempotencyKeyHolder,
) {

    fun process(idempotencyKey: String, endpointSettings: IdempotentEndpointSettings): IdempotencyProcessingStatus {
        if (isLockTtlSpecified(endpointSettings) && !isLockAcquired(idempotencyKey)) {
            serveIdempotencyKey(idempotencyKey, endpointSettings)
            return IdempotencyProcessingStatus.TTL_SPECIFIED
        }

        repeat(endpointSettings.retryCount) { retry ->
            if (isLockAcquired(idempotencyKey)) {
                endpointSettings.retryDelay?.also {
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
        idempotencyKeyHolder.remove(idempotencyKey)
    }

    private fun isLockAcquired(idempotencyKey: String): Boolean {
        return idempotencyKeyHolder.get(idempotencyKey) != null
    }
}