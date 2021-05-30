package ru.phoenigm.idempotency.curator.configuration.reactive.data

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.core.data.inmemory.InMemoryIdempotencyKeyHolder

@ConditionalOnMissingBean(IdempotencyKeyHolder::class)
@Configuration
open class IdempotencyKeyHolderAutoConfiguration {

    @Bean
    open fun idempotencyKeyHolder() = InMemoryIdempotencyKeyHolder()

}
