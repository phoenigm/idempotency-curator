package ru.phoenigm.idempotency.curator.core

import mu.KotlinLogging
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils.MethodCallback
import org.springframework.web.bind.annotation.*
import java.lang.reflect.Method

@Component
class IdempotentCallback(
    private val idempotentEndpoints: MutableSet<HttpData>
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
        logger.info { "Method ${method.name}" }
        annotationMappings.forEach { (annotationClass, httpMethod) ->
            if (!method.isAnnotationPresent(annotationClass.java)) {
                return@forEach
            }
            val endpoints = when (val annotation = method.getAnnotation(annotationClass.java)) {
                is PostMapping -> annotation.value
                is GetMapping -> annotation.value
                is PutMapping -> annotation.value
                is PatchMapping -> annotation.value
                is DeleteMapping -> annotation.value
                else -> return@forEach
            }
            createHttpData(httpMethod, *endpoints)
        }

    }

    private fun createHttpData(httpMethod: String, vararg endpoints: String) {
        endpoints.forEach {
            val httpData = HttpData(it, httpMethod)
            logger.info { "Create http date: $httpData" }
            idempotentEndpoints.add(httpData)
        }
    }
}