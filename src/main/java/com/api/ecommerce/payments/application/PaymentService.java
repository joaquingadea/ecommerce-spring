package com.api.ecommerce.payments.application;

import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.orders.domain.OrderStatus;
import com.api.ecommerce.payments.domain.Payment;
import com.api.ecommerce.payments.domain.PaymentStatus;
import com.api.ecommerce.payments.infrastructure.persistence.IPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;


    public PaymentService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createAttemptPayment(Order order){
        Payment payment = new Payment(
                null,
                null,
                null,
                LocalDateTime.now(),
                order.getTotal(),
                PaymentStatus.PENDING,
                null
        );
        order.addPaymentAttempt(payment);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public void markAsPaid(Long paymentId) {
        Payment paymentRepo = paymentRepository.findById(paymentId).orElseThrow();
        paymentRepo.setStatus(PaymentStatus.APPROVED);
        paymentRepo.getOrder().setStatus(OrderStatus.PAID);
    }

    @Override
    public void markAsRejected(Long paymentId) {
        Payment paymentRepo = paymentRepository.findById(paymentId).orElseThrow();
        paymentRepo.setStatus(PaymentStatus.REJECTED);
    }

    @Override
    public Payment getOrCreateAttemptPayment(Order order){

        boolean approvedPayment = order.getPaymentAttempts().stream()
                .anyMatch(payment -> payment.getStatus().equals(PaymentStatus.APPROVED));

        if (approvedPayment) {
            throw new IllegalStateException("Payment already paid!");
        }

        return order.getPaymentAttempts().stream()
                .filter(payment -> payment.getStatus().equals(PaymentStatus.PENDING))
                .findFirst()
                .orElseGet(() -> createAttemptPayment(order));
    }

    @Override
    public void attachPreferenceId(Payment payment, String prefId) {
        payment.setMercadoPagoPreferenceId(prefId);
        paymentRepository.save(payment);
    }

    @Override
    public void attachMercadoPagoPaymentId(Payment payment, String mercadoPagoPaymentId) {
        payment.setMercadoPagoPaymentId(Long.parseLong(mercadoPagoPaymentId));
        paymentRepository.save(payment);
    }

    @Override
    public Payment findById(String s) {
        return paymentRepository.findById(Long.parseLong(s)).orElseThrow();
    }
}
