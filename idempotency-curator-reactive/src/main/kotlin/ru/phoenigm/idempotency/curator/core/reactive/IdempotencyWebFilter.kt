package ru.phoenigm.idempotency.curator.core.reactive

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.phoenigm.idempotency.curator.core.HttpEndpoint
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyConfig
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyProcessor
import ru.phoenigm.idempotency.curator.core.IdempotencyProcessingStatus.*
import ru.phoenigm.idempotency.curator.core.IdempotentEndpointResolver

@Order(Int.MAX_VALUE)
@Component
class IdempotencyWebFilter(
    private val idempotencyKeyProcessor: IdempotencyKeyProcessor,
    private val idempotentEndpointResolver: IdempotentEndpointResolver,
    private val config: IdempotencyKeyConfig
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val idempotencyKey = exchange.request.headers[config.header]?.first()

        if (idempotencyKey == null) {
            chain.filter(exchange)
            return Mono.empty()
        }
        return handleIdempotencyHeader(idempotencyKey, exchange, chain)
    }

    private fun handleIdempotencyHeader(
        idempotencyKey: String,
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        val request = exchange.request
        val response = exchange.response
        val httpEndpoint = HttpEndpoint(request.methodValue, request.uri.path)

        idempotentEndpointResolver.getSettingsForEndpoint(httpEndpoint)?.let {
            return when (idempotencyKeyProcessor.process(idempotencyKey, it)) {
                LOCKED -> {
                    val buffer = response.bufferFactory().wrap(it.errorMessage.toByteArray())
                    response.rawStatusCode = it.errorHttpCode
                    response.writeWith(Flux.just(buffer))
                }
                FREE -> {
                    chain.filter(exchange).also {
                        idempotencyKeyProcessor.releaseLock(idempotencyKey)
                    }
                }
                TTL_SPECIFIED -> {
                    chain.filter(exchange)
                }
            }
        }
        return Mono.empty()
    }

}
