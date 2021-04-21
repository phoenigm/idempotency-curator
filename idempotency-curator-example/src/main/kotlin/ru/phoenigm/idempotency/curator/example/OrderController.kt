package ru.phoenigm.idempotency.curator.example

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/orders")
@RestController
class OrderController {

    @PostMapping
    fun orderTaxi(@RequestBody orderTaxiRequest: OrderTaxiRequest): ResponseEntity<OrderTaxiResponse> {
        println(orderTaxiRequest)
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

