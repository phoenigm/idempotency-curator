package ru.phoenigm.idempotency.curator.example

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PaymentService {

    companion object {
        var logger = KotlinLogging.logger { }
    }

    fun makePayment(request: PaymentRequest): PaymentResponse {
        logger.info { "Processing payment: ${request.data}" }
        Thread.sleep(5000)
        logger.info { "Processing payment was successful: ${request.data}" }
        return PaymentResponse("success")
    }
}