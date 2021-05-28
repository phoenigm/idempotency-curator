package ru.phoenigm.idempotency.curator.core

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Idempotent(
    val errorHttpCode: Int = 409,
    val errorMessage: String = "Declined idempotent request",
    val idempotencyKeyTtl: String = "PT0S",
    val retryCount: Int = 1,
    val retryDelay: String = "PT0S",
)
