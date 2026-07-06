package com.api.ecommerce.payments.application;

import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.payments.domain.Payment;

import java.util.Optional;

public interface IPaymentService {
    void markAsPaid(Long paymentId);
    void markAsRejected(Long paymentId);
    Payment getOrCreateAttemptPayment(Order order);
    void attachPreferenceId(Payment payment, String id);
}
