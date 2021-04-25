package ru.phoenigm.idempotency.curator.core

import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils.MethodCallback
import org.springframework.web.bind.annotation.PostMapping
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentSkipListSet

@Component
class IdempotentCallback(private val allowedEndpoints: ConcurrentMap<HttpData, String>) : MethodCallback {

    override fun doWith(method: Method) {
        if (method.name == "orderTaxi") {
            println(method.annotations)
        }

        if (!method.isAnnotationPresent(Idempotent::class.java)) {
            return
        }
        println("1 $method")
        if (method.isAnnotationPresent(PostMapping::class.java)) {
            println("post mapping present")
            val postMappingAnnotation = method.getAnnotation(PostMapping::class.java)

            /*if (postMappingAnnotation.path.isEmpty()) {
                allowedEndpoints.add(HttpData("/", "post"))
            }*/

            postMappingAnnotation.value.forEach {
                val httpMethod = "POST"
                val httpData = HttpData(it, httpMethod)
                println("2 $httpData")
                allowedEndpoints[HttpData(it, httpMethod)] = UUID.randomUUID().toString()
            }
        }
    }
}