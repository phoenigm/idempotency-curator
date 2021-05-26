package ru.phoenigm.idempotency.curator.starter.configuration.data

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.redis.RedisIdempotencyKeyHolder

@ConditionalOnMissingBean(RedisTemplate::class)
@Configuration
open class RedisAutoConfiguration {

    @Bean
    open fun lettuceConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory()
    }

    @Bean
    open fun redisTemplate(): RedisTemplate<String, HttpData> {
        val template = RedisTemplate<String, HttpData>()
        template.connectionFactory = lettuceConnectionFactory()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(HttpData::class.java)
        return template
    }

    @Bean
    open fun idempotencyKeyHolder(redisTemplate: RedisTemplate<String, HttpData>) =
        RedisIdempotencyKeyHolder(redisTemplate)
}