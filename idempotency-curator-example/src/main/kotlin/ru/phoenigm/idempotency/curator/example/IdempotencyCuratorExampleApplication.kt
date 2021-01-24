package ru.phoenigm.idempotency.curator.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IdempotencyCuratorExampleApplication

fun main(args: Array<String>) {
    runApplication<IdempotencyCuratorExampleApplication>(*args)
}
