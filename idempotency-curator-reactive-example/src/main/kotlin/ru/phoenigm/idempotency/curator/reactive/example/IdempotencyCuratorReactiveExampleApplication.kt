package ru.phoenigm.idempotency.curator.reactive.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IdempotencyCuratorReactiveExampleApplication

fun main(args: Array<String>) {
    runApplication<IdempotencyCuratorReactiveExampleApplication>(*args)
}
