package ru.phoenigm.idempotency.curator.core.data.hazelcast

import com.hazelcast.map.IMap
import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class HazelcastIdempotencyKeyHolder(
    private val map: IMap<String, HttpData>
) : IdempotencyKeyHolder {

    override fun put(idempotencyKey: String, httpData: HttpData, ttl: Duration?) {
        if (ttl == null) {
            map.put(idempotencyKey, httpData)
        } else {
            map.put(idempotencyKey, httpData, ttl.toMillis(), TimeUnit.MILLISECONDS)
        }
    }

    override fun get(idempotencyKey: String): HttpData? {
        return map[idempotencyKey]
    }

    override fun remove(idempotencyKey: String): HttpData? {
        return map.remove(idempotencyKey)
    }
}