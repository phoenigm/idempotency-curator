package ru.phoenigm.idempotency.curator.configuration.reactive.data

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.redis.RedisIdempotencyKeyHolder

@ConditionalOnProperty(
    prefix = "spring",
    name = ["idempotency.curator.keyStorage"],
    havingValue = "REDIS",
    matchIfMissing = false
)
@ConditionalOnBean(RedisTemplate::class)
@Configuration
open class RedisAutoConfiguration {

    companion object {
        val logger = KotlinLogging.logger { }
    }

    @ConditionalOnMissingBean(RedisConnectionFactory::class)
    @Bean
    open fun lettuceConnectionFactory() = LettuceConnectionFactory()

    @Bean
    open fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, HttpData> {
        val template = RedisTemplate<String, HttpData>()
        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(HttpData::class.java)
        return template
    }

    @Bean
    open fun idempotencyKeyHolder(redisTemplate: RedisTemplate<String, HttpData>) =
        RedisIdempotencyKeyHolder(redisTemplate).also {
            logger.info { "Initialize bean RedisIdempotencyKeyHolder" }
        }
}