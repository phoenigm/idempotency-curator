package ru.phoenigm.idempotency.curator.core

import org.springframework.stereotype.Component

@Component
class IdempotentEndpointResolver(
    private val idempotentEndpoints: Set<IdempotentEndpointSettings>
) {

    fun getSettingsForEndpoint(endpoint: HttpEndpoint): IdempotentEndpointSettings? =
        idempotentEndpoints.find {
            it.url == endpoint.url && it.method == it.method
        }
}
