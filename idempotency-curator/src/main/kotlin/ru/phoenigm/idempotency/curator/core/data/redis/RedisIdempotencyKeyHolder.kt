package ru.phoenigm.idempotency.curator.core.data.redis

import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder

@Component
open class RedisIdempotencyKeyHolder(
    redisTemplate: RedisTemplate<String, HttpData>
) : IdempotencyKeyHolder {
    private val hashOperations: HashOperations<String, String, HttpData> = redisTemplate.opsForHash()

    companion object {
        private const val key = "IdempotentKey"
    }

    override fun put(idempotencyKey: String, httpData: HttpData) {
        hashOperations.put(key, idempotencyKey, httpData)
    }

    override fun get(idempotencyKey: String): HttpData? {
        return hashOperations.get(key, idempotencyKey)
    }

    override fun remove(idempotencyKey: String): HttpData? {
        val httpData = get(idempotencyKey)
        hashOperations.delete(key, idempotencyKey)
        return httpData
    }
}