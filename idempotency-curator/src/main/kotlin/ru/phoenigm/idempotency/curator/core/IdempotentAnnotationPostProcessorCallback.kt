package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils.MethodCallback
import org.springframework.web.bind.annotation.*
import java.lang.reflect.Method
import java.time.Duration

@Component
class IdempotentAnnotationPostProcessorCallback(
    private val idempotentEndpoints: MutableSet<IdempotentEndpointSettings>
) : MethodCallback {

    companion object {
        private val logger = KotlinLogging.logger { }
        private val annotationMappings = mapOf(
            Pair(GetMapping::class, HttpMethod.GET.name),
            Pair(PostMapping::class, HttpMethod.POST.name),
            Pair(PutMapping::class, HttpMethod.PUT.name),
            Pair(PatchMapping::class, HttpMethod.PATCH.name),
            Pair(DeleteMapping::class, HttpMethod.DELETE.name)
        )
    }

    override fun doWith(method: Method) {
        if (!method.isAnnotationPresent(Idempotent::class.java)) {
            return
        }
        val idempotentAnnotation = method.getAnnotation(Idempotent::class.java)
        collectHttpEndpoints(method).forEach {
            markEndpointAsIdempotent(
                IdempotentEndpointSettings(
                    url = it.url,
                    method = it.method,
                    ttl = parseDuration(idempotentAnnotation.idempotencyKeyTtl),
                    errorHttpCode = idempotentAnnotation.errorHttpCode,
                    errorMessage = idempotentAnnotation.errorMessage,
                    retryDelay = parseDuration(idempotentAnnotation.retryDelay),
                    retryCount = idempotentAnnotation.retryCount
                )
            )
        }
    }

    private fun parseDuration(duration: String): Duration? =
        when (val parsedDuration = Duration.parse(duration)) {
            Duration.ZERO -> null
            else -> parsedDuration
        }

    private fun collectHttpEndpoints(method: Method): List<HttpEndpoint> {
        annotationMappings.forEach { (annotationClass, httpMethod) ->
            if (!method.isAnnotationPresent(annotationClass.java)) {
                return@forEach
            }
            return when (val annotation = method.getAnnotation(annotationClass.java)) {
                is PostMapping -> annotation.value
                is GetMapping -> annotation.value
                is PutMapping -> annotation.value
                is PatchMapping -> annotation.value
                is DeleteMapping -> annotation.value
                else -> return@forEach
            }.map {
                HttpEndpoint(httpMethod, it)
            }
        }
        return emptyList()
    }

    private fun markEndpointAsIdempotent(settings: IdempotentEndpointSettings) {
        logger.info { "Mark endpoint as idempotent: $settings" }
        idempotentEndpoints.add(settings)
    }
}