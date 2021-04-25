package ru.phoenigm.idempotency.curator.example

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.phoenigm.idempotency.curator.core.Idempotent

@RestController
class OrderController {

    private val logger = KotlinLogging.logger {}

    @Idempotent
    @PostMapping("/orders")
    fun orderTaxi(@RequestBody orderTaxiRequest: OrderTaxiRequest): ResponseEntity<OrderTaxiResponse> {
        logger.info { "Receive request: $orderTaxiRequest" }
        Thread.sleep(5000)
        return ResponseEntity.ok(OrderTaxiResponse("success"))
    }
}

data class OrderTaxiRequest(
    val requester: String
)

data class OrderTaxiResponse(
    val status: String
)

