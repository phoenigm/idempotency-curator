package ru.phoenigm.idempotency.curator.core

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Idempotent(val header: String = "IdempotencyKey")
