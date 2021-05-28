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
import ru.phoenigm.idempotency.curator.starter.configuration.data.HazelcastAutoConfiguration
import ru.phoenigm.idempotency.curator.starter.configuration.data.IdempotencyKeyHolderAutoConfiguration
import ru.phoenigm.idempotency.curator.starter.configuration.data.RedisAutoConfiguration
import java.util.concurrent.ConcurrentHashMap

@Import(
    RedisAutoConfiguration::class,
    HazelcastAutoConfiguration::class,
    IdempotencyKeyHolderAutoConfiguration::class
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
    ) = IdempotencyKeyProcessor(idempotencyKeyHolder)

    @Bean
    open fun idempotencyKeyConfig(
        idempotencyCuratorProperties: IdempotencyCuratorProperties
    ) = idempotencyCuratorProperties.let {
        IdempotencyKeyConfig(
            lockTtl = it.lockTtl,
            retryCount = it.retryCount,
            retryDelay = it.retryDelay,
            header = it.header,
            errorMessage = it.errorMessage,
            errorHttpCode = it.errorHttpCode
        )
    }

    @Bean
    open fun idempotentEndpoints(): MutableSet<IdempotentEndpointSettings> = ConcurrentHashMap.newKeySet()

    @Bean
    open fun callback(
        idempotentEndpoints: MutableSet<IdempotentEndpointSettings>,
    ) = IdempotentAnnotationPostProcessorCallback(idempotentEndpoints)

    @Bean
    open fun idempotentAnnotationPostProcessor(annotationPostProcessorCallback: IdempotentAnnotationPostProcessorCallback) =
        IdempotentAnnotationPostProcessor(annotationPostProcessorCallback)

    @Bean
    open fun idempotentCallResolver(idempotentEndpoints: Set<IdempotentEndpointSettings>) =
        IdempotentEndpointResolver(idempotentEndpoints)

}