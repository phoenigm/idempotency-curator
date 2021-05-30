package ru.phoenigm.idempotency.curator.configuration.reactive

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.phoenigm.idempotency.curator.configuration.reactive.data.HazelcastAutoConfiguration
import ru.phoenigm.idempotency.curator.configuration.reactive.data.IdempotencyKeyHolderAutoConfiguration
import ru.phoenigm.idempotency.curator.configuration.reactive.data.RedisAutoConfiguration
import ru.phoenigm.idempotency.curator.core.*
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.core.reactive.IdempotencyWebFilter
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
@EnableConfigurationProperties(IdempotencyCuratorProperties::class)
@Configuration
open class IdempotencyCuratorReactiveAutoConfiguration {

    @ConditionalOnMissingBean(IdempotencyWebFilter::class)
    @Bean
    open fun idempotencyFilter(
        idempotencyKeyProcessor: IdempotencyKeyProcessor,
        idempotentEndpointResolver: IdempotentEndpointResolver,
        idempotencyKeyConfig: IdempotencyKeyConfig
    ): IdempotencyWebFilter = IdempotencyWebFilter(
        idempotencyKeyProcessor,
        idempotentEndpointResolver,
        idempotencyKeyConfig
    )

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
