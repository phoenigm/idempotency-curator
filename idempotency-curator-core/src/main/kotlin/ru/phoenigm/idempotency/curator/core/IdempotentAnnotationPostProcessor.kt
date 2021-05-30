package ru.phoenigm.idempotency.curator.core

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils

@Component
class IdempotentAnnotationPostProcessor(
    private val annotationPostProcessorCallback: IdempotentAnnotationPostProcessorCallback
) : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val cls = bean::class.java
        ReflectionUtils.doWithMethods(cls, annotationPostProcessorCallback)
        return bean
    }
}
