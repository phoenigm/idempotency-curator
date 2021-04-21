package ru.phoenigm.idempotency.curator.spring

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyProcessor
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(Int.MAX_VALUE)
@Component
class IdempotencyFilter(
    private val idempotencyKeyProcessor: IdempotencyKeyProcessor
) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val idempotencyKey = httpRequest.getHeader("IdempotencyKey")
        val url = httpRequest.requestURL.toString()
        val method = httpRequest.method
        val httpData = HttpData(url, method)

        if (!idempotencyKeyProcessor.process(httpData, idempotencyKey)) {
            httpResponse.sendError(409)
        } else {
            chain.doFilter(request, response)
            idempotencyKeyProcessor.releaseLock(httpData)
        }
    }

}