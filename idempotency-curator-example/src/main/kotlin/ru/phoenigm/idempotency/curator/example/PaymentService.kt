package ru.phoenigm.idempotency.curator.example

import org.springframework.stereotype.Service

@Service
class PaymentService {

    fun makePayment(request: PaymentRequest) : PaymentResponse {
        return PaymentResponse("success")
    }
}

data class PaymentRequest(
    val data: String
)

data class PaymentResponse(
    val status: String
)