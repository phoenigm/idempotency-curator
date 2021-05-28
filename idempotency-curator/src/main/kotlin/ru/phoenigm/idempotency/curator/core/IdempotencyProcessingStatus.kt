package ru.phoenigm.idempotency.curator.core

enum class IdempotencyProcessingStatus {
    TTL_SPECIFIED,
    LOCKED,
    FREE
}