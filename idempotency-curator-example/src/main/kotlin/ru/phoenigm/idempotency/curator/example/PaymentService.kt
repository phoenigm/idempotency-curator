package ru.phoenigm.idempotency.curator.example

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PaymentService {

    companion object {
        var logger = KotlinLogging.logger { }
    }

    fun makePayment(request: PaymentRequest): PaymentResponse {
        logger.info { "Processing payment: $request" }
        Thread.sleep(request.amount)
        logger.info { "Processing payment was successful: $request" }
        return PaymentResponse("success")
    }
}