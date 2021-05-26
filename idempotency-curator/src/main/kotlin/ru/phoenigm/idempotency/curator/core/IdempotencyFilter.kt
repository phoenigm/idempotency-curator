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
        val url = httpRequest.servletPath
        val method = httpRequest.method
        val httpData = HttpData(url, method)

        if (!idempotentEndpointResolver.isIdempotent(httpData)) {
            chain.doFilter(request, response)
            return
        }
        if (!idempotencyKeyProcessor.process(idempotencyKey, httpData)) {
            httpResponse.sendError(config.errorHttpCode, config.errorMessage)
        } else {
            chain.doFilter(request, response)
            idempotencyKeyProcessor.releaseLock(idempotencyKey)
        }
    }

}