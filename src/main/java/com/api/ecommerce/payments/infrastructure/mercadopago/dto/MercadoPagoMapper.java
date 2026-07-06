package com.api.ecommerce.payments.infrastructure.mercadopago.dto;

import com.api.ecommerce.payments.domain.PaymentStatus;
import com.api.ecommerce.payments.dto.request.CreatePaymentDTO;
import com.api.ecommerce.payments.dto.response.ExternalPaymentDetailsDTO;
import com.api.ecommerce.payments.infrastructure.mercadopago.dto.request.CreatePreferenceDTO;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MercadoPagoMapper {

    @Value("${app.url}")
    private String urlBackend;

    public PaymentStatus mapStatus(String mpStatus) {
        if (mpStatus == null) {
            return PaymentStatus.PENDING;
        }
        return switch (mpStatus.toLowerCase(Locale.ROOT)) {

            case "approved" -> PaymentStatus.APPROVED;
            case "rejected","cancelled" -> PaymentStatus.REJECTED;
            case "in_process","pending", "authorized" -> PaymentStatus.PENDING;

            default -> throw new IllegalArgumentException(
                    "Unknown MercadoPago status: " + mpStatus
            );
        };
    }

    public ExternalPaymentDetailsDTO toExternalPaymentDetailsDTO(Payment paymentMP){
        return new ExternalPaymentDetailsDTO(
                paymentMP.getId().toString(),
                mapStatus(paymentMP.getStatus()),
                paymentMP.getTransactionAmount(),
                paymentMP.getExternalReference(),
                paymentMP.getPaymentTypeId(),
                paymentMP.getDateCreated().toLocalDateTime(),
                paymentMP.getDateApproved().toLocalDateTime()
        );
    }

    public CreatePreferenceDTO toCreatePreferenceDTO(CreatePaymentDTO dto){

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(dto.successUrl())
                .pending(dto.pendingUrl())
                .failure(dto.failureUrl())
                .build();

        List<PreferenceItemRequest> items = dto.items().stream()
                .map(p -> PreferenceItemRequest.builder()
                        .id(p.orderDetailId())
                        .title(p.name())
                        .description(p.description())
                        .pictureUrl(p.firstImageUrl())
                        .quantity(p.quantity())
                        .unitPrice(p.unitPrice())
                        .build()
                )
                .toList();

        return new CreatePreferenceDTO(
                dto.paymentId().toString(),
                items,
                backUrls
        );
    }
}
