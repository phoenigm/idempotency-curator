package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class IdempotentEndpointResolver(private val idempotentEndpoints: Set<HttpData>) {

    companion object {
        val logger = KotlinLogging.logger { }
    }

    fun isIdempotent(httpData: HttpData): Boolean {
        return idempotentEndpoints.contains(httpData).also {
            logger.info { "Endpoint with http data = $httpData is idempotent = $it" }
        }
    }
}