package ru.phoenigm.idempotency.curator.starter.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.idempotency.curator")
data class IdempotencyCuratorProperties(
    val enabled: Boolean = true,
    val lockTtl: Duration = Duration.ZERO,
    val retryCount: Int = 10,
    val retryDelay: Duration = Duration.ofMillis(100),
    val header: String = "IdempotencyKey",
    val errorMessage: String = "Idempotent call declined",
    val errorHttpCode: Int = 409
)