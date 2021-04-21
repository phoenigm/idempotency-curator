package ru.phoenigm.idempotency.curator.example

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyConfig
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyHolder
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyHolderImpl
import ru.phoenigm.idempotency.curator.core.IdempotencyKeyProcessor
import ru.phoenigm.idempotency.curator.spring.IdempotencyFilter
import java.time.Duration

@Configuration
open class FilterConfiguration {

    @Bean
    open fun idempotencyFilter(
        idempotencyKeyProcessor: IdempotencyKeyProcessor
    ): FilterRegistrationBean<IdempotencyFilter> {
        val registrationBean = FilterRegistrationBean<IdempotencyFilter>()
        registrationBean.filter = IdempotencyFilter(idempotencyKeyProcessor)
        registrationBean.addUrlPatterns("/orders")
        return registrationBean
    }

    @Bean
    open fun idempotencyKeyProcessor(
        idempotencyKeyHolder: IdempotencyKeyHolder,
        idempotencyKeyConfig: IdempotencyKeyConfig
    ) = IdempotencyKeyProcessor(idempotencyKeyHolder, idempotencyKeyConfig)

    @Bean
    open fun idempotencyKeyHolder() = IdempotencyKeyHolderImpl()

    @Bean
    open fun dempotencyKeyConfig() = IdempotencyKeyConfig(Duration.ZERO, 30, Duration.ZERO)
}