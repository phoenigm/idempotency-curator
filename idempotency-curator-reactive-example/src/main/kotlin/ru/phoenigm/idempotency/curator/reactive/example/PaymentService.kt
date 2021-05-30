package ru.phoenigm.idempotency.curator.reactive.example

import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService {

    companion object {
        var logger = KotlinLogging.logger { }
    }

    fun makePayment(request: PaymentRequest): Mono<PaymentResponse> {
        logger.info { "Processing payment: $request" }
        Thread.sleep(request.amount)
        logger.info { "Processing payment was successful: $request" }
        return Mono.just(PaymentResponse("success"))
    }
}