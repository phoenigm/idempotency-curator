package ru.phoenigm.idempotency.curator.spring

import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import java.util.concurrent.ConcurrentMap

@Component
class IdempotentCallResolver(private val allowedEndpoints: ConcurrentMap<HttpData, String>) {

    fun isIdempotent(httpData: HttpData): Boolean {
        println("is idempotent ${allowedEndpoints.contains(httpData)}")
        return allowedEndpoints.contains(httpData)
    }
}