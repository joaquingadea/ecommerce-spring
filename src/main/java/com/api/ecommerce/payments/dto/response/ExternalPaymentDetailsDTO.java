package com.api.ecommerce.payments.dto.response;

import com.api.ecommerce.payments.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExternalPaymentDetailsDTO(
        String id,
        PaymentStatus status,
        BigDecimal amount,
        String internalPaymentId,
        String paymentType,       // credit_card, debit_card, etc.
        LocalDateTime createdAt,
        LocalDateTime approvedAt // puede ser null si no está aprobado
) {
}
