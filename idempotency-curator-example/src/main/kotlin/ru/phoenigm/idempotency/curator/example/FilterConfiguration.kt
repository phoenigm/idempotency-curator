package ru.phoenigm.idempotency.curator.example

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.phoenigm.idempotency.curator.core.*
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.core.data.inmemory.InMemoryIdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.spring.IdempotencyFilter
import ru.phoenigm.idempotency.curator.spring.IdempotentCallResolver
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Configuration
open class FilterConfiguration {

    @Bean
    open fun idempotencyFilter(
        idempotencyKeyProcessor: IdempotencyKeyProcessor,
        idempotentCallResolver: IdempotentCallResolver
    ): FilterRegistrationBean<IdempotencyFilter> {
        val registrationBean = FilterRegistrationBean<IdempotencyFilter>()
        registrationBean.filter = IdempotencyFilter(idempotencyKeyProcessor, idempotentCallResolver)
        return registrationBean
    }

    @Bean
    open fun hazelcastInstance(): HazelcastInstance = Hazelcast.newHazelcastInstance()

    @Bean
    open fun idempotencyKeyProcessor(
        idempotencyKeyHolder: IdempotencyKeyHolder,
        idempotencyKeyConfig: IdempotencyKeyConfig
    ) = IdempotencyKeyProcessor(idempotencyKeyHolder, idempotencyKeyConfig)

    @Bean
    open fun idempotencyKeyHolder() = InMemoryIdempotencyKeyHolder()

    @Bean
    open fun idempotencyKeyConfig() = IdempotencyKeyConfig(Duration.ZERO, 30, Duration.ZERO)

    @Bean
    open fun allowedEndpoints() = ConcurrentHashMap<HttpData, String>()

    @Bean
    open fun callback(allowedEndpoints: ConcurrentMap<HttpData, String>) = IdempotentCallback(allowedEndpoints)

    @Bean
    open fun idempotentAnnotationPostProcessor(callback: IdempotentCallback) =
        IdempotentAnnotationPostProcessor(callback)

    @Bean
    open fun idempotentCallResolver(allowedEndpoints: ConcurrentMap<HttpData, String>) = IdempotentCallResolver(allowedEndpoints)
}