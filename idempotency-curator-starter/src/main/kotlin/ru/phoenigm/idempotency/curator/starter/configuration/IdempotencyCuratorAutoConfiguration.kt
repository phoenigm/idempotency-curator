package ru.phoenigm.idempotency.curator.starter.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.phoenigm.idempotency.curator.core.*
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.starter.configuration.data.IdempotencyKeyHolderAutoConfiguration
import ru.phoenigm.idempotency.curator.starter.configuration.data.RedisAutoConfiguration
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Import(
    IdempotencyKeyHolderAutoConfiguration::class,
    RedisAutoConfiguration::class
)
@ConditionalOnProperty(
    prefix = "spring",
    name = ["idempotency.curator.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@ConditionalOnMissingBean(IdempotencyKeyProcessor::class)
@ConditionalOnMissingFilterBean(IdempotencyFilter::class)
@EnableConfigurationProperties(IdempotencyCuratorProperties::class)
@Configuration
open class IdempotencyCuratorAutoConfiguration {

    @Bean
    open fun idempotencyFilter(
        idempotencyKeyProcessor: IdempotencyKeyProcessor,
        idempotentEndpointResolver: IdempotentEndpointResolver,
        idempotencyKeyConfig: IdempotencyKeyConfig
    ): FilterRegistrationBean<IdempotencyFilter> {
        return FilterRegistrationBean<IdempotencyFilter>()
            .apply {
                filter = IdempotencyFilter(
                    idempotencyKeyProcessor,
                    idempotentEndpointResolver,
                    idempotencyKeyConfig
                )
            }
    }

    @Bean
    open fun idempotencyKeyProcessor(
        idempotencyKeyHolder: IdempotencyKeyHolder,
        idempotencyKeyConfig: IdempotencyKeyConfig
    ) = IdempotencyKeyProcessor(idempotencyKeyHolder, idempotencyKeyConfig)

    @Bean
    open fun idempotencyKeyConfig(
        idempotencyCuratorProperties: IdempotencyCuratorProperties
    ) = idempotencyCuratorProperties.let {
        IdempotencyKeyConfig(
            lockTtl = Duration.ZERO,
            retryCount = 1,
            retryDelay = Duration.ZERO,
            header = "IdempotencyKey",
            errorMessage = "sd",
            errorHttpCode = 409
        )
    }

    @Bean
    open fun idempotentEndpoints(): MutableSet<HttpData> = ConcurrentHashMap.newKeySet()

    @Bean
    open fun callback(idempotentEndpoints: MutableSet<HttpData>) = IdempotentCallback(idempotentEndpoints)

    @Bean
    open fun idempotentAnnotationPostProcessor(callback: IdempotentCallback) =
        IdempotentAnnotationPostProcessor(callback)

    @Bean
    open fun idempotentCallResolver(idempotentEndpoints: Set<HttpData>) =
        IdempotentEndpointResolver(idempotentEndpoints)

}