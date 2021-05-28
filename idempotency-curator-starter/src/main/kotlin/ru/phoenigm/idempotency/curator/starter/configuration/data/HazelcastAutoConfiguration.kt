package ru.phoenigm.idempotency.curator.starter.configuration.data

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.phoenigm.idempotency.curator.core.HttpData
import ru.phoenigm.idempotency.curator.core.data.hazelcast.HazelcastIdempotencyKeyHolder

@ConditionalOnProperty(
    prefix = "spring",
    name = ["idempotency.curator.keyStorage"],
    havingValue = "HAZELCAST",
    matchIfMissing = false
)
@ConditionalOnClass(Hazelcast::class)
@Configuration
open class HazelcastAutoConfiguration {

    companion object {
        val logger = KotlinLogging.logger { }
    }

    @ConditionalOnMissingBean(HazelcastInstance::class)
    @Bean
    open fun hazelcastInstance(): HazelcastInstance = Hazelcast.newHazelcastInstance()

    @Bean
    open fun idempotencyKeyMap(hazelcastInstance: HazelcastInstance) =
        hazelcastInstance.getMap<String, HttpData>("idempotency")

    @Bean
    open fun idempotencyKeyHolder(map: IMap<String, HttpData>) =
        HazelcastIdempotencyKeyHolder(map).also {
            logger.info { "Initialize bean HazelcastIdempotencyKeyHolder" }
        }
}