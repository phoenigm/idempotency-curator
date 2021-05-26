package ru.phoenigm.idempotency.curator.core.data.hazelcast

import org.springframework.stereotype.Component
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.IdempotencyKeyHolder

@Component
class HazelcastIdempotencyKeyHolder : IdempotencyKeyHolder {

    override fun put(idempotencyKey: String, httpData: HttpData) {
        TODO("Not yet implemented")
    }

    override fun get(idempotencyKey: String): HttpData? {
        TODO("Not yet implemented")
    }

    override fun remove(idempotencyKey: String): HttpData? {
        TODO("Not yet implemented")
    }
}