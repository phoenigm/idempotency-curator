package ru.phoenigm.idempotency.curator.reactive.example

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.phoenigm.idempotency.curator.core.Idempotent

@RestController
class PaymentController(
    private val paymentService: PaymentService
) {

    @Idempotent(retryCount = 3, retryDelay = "PT0.3S")
    @PostMapping("/reactive/payments")
    fun makePayment(@RequestBody request: PaymentRequest): Mono<PaymentResponse> {
        return paymentService.makePayment(request)
    }

}

data class PaymentRequest(
    val amount: Long,
)

data class PaymentResponse(
    val status: String
)