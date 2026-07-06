package com.api.ecommerce.payments.infrastructure.mercadopago;

import com.api.ecommerce.payments.application.IPaymentService;
import com.api.ecommerce.payments.domain.Payment;
import com.api.ecommerce.payments.domain.PaymentGateway;
import com.api.ecommerce.payments.dto.response.ExternalPaymentDetailsDTO;
import com.api.ecommerce.payments.infrastructure.mercadopago.dto.request.MercadoPagoNotificationDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MercadoPagoWebhookService {

    private final PaymentGateway paymentGateway;
    private final IPaymentService paymentService;

    public MercadoPagoWebhookService(PaymentGateway paymentGateway, IPaymentService paymentService) {
        this.paymentGateway = paymentGateway;
        this.paymentService = paymentService;
    }

    public void process(MercadoPagoNotificationDTO notification) {
        if ("payment".equals(notification.getType())) {

            String mercadoPagoPaymentId = notification.getData().getId();

            ExternalPaymentDetailsDTO paymentDetailsDTO = paymentGateway.getPayment(mercadoPagoPaymentId);

            Payment payment = paymentService.findById(paymentDetailsDTO.internalPaymentId());

            paymentService.attachMercadoPagoPaymentId(payment, mercadoPagoPaymentId);

            if (!"payment".equals(notification.getType())) {
                return;
            }

            switch (paymentDetailsDTO.status()) {
                case APPROVED -> paymentService.markAsPaid(Long.parseLong(paymentDetailsDTO.internalPaymentId()));
                case REJECTED -> paymentService.markAsRejected(Long.parseLong(paymentDetailsDTO.internalPaymentId()));
            }
        }
    }
}
