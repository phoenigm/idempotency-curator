package ru.phoenigm.idempotency.curator.core

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(Int.MAX_VALUE)
@Component
class IdempotencyFilter(
    private val idempotencyKeyProcessor: IdempotencyKeyProcessor,
    private val idempotentEndpointResolver: IdempotentEndpointResolver,
    private val config: IdempotencyKeyConfig
) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val idempotencyKey = httpRequest.getHeader(config.header)

        if (idempotencyKey == null) {
            chain.doFilter(request, response)
            return
        }
        handleIdempotencyHeader(idempotencyKey, httpRequest, httpResponse, chain)
    }

    private fun handleIdempotencyHeader(
        idempotencyKey: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val httpEndpoint = HttpEndpoint(request.method, request.servletPath)

        idempotentEndpointResolver.getSettingsForEndpoint(httpEndpoint)?.let {
            when (idempotencyKeyProcessor.process(idempotencyKey, it)) {
                IdempotencyProcessingStatus.LOCKED -> {
                    response.sendError(it.errorHttpCode, it.errorMessage)
                }
                IdempotencyProcessingStatus.FREE -> {
                    chain.doFilter(request, response)
                    idempotencyKeyProcessor.releaseLock(idempotencyKey)
                }
                IdempotencyProcessingStatus.TTL_SPECIFIED -> {
                    chain.doFilter(request, response)
                }
            }
        }
    }

}
