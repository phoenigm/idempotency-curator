package ru.phoenigm.idempotency.curator.core.data.redis

import mu.KotlinLogging
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import java.time.Duration

@Component
open class RedisIdempotencyKeyHolder(
    private val redisTemplate: RedisTemplate<String, HttpData>
) : IdempotencyKeyHolder {

    private val hashOperations: HashOperations<String, String, HttpData> = redisTemplate.opsForHash()

    override fun put(idempotencyKey: String, httpData: HttpData, ttl: Duration?) {
        hashOperations.put(idempotencyKey, idempotencyKey, httpData)
        ttl?.also {
            redisTemplate.expire(idempotencyKey, ttl)
        }
    }

    override fun get(idempotencyKey: String): HttpData? {
        return hashOperations.get(idempotencyKey, idempotencyKey)
    }

    override fun remove(idempotencyKey: String): HttpData? {
        val httpData = get(idempotencyKey)
        hashOperations.delete(idempotencyKey, idempotencyKey)
        return httpData
    }
}