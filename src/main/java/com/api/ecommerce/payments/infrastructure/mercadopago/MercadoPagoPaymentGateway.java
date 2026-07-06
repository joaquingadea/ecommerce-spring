package com.api.ecommerce.payments.infrastructure.mercadopago;

import com.api.ecommerce.payments.domain.PaymentGateway;
import com.api.ecommerce.payments.dto.request.CreatePaymentDTO;
import com.api.ecommerce.payments.dto.response.PaymentCreationResultDTO;
import com.api.ecommerce.payments.dto.response.ExternalPaymentDetailsDTO;
import com.api.ecommerce.payments.infrastructure.mercadopago.dto.MercadoPagoMapper;
import com.api.ecommerce.payments.infrastructure.mercadopago.dto.request.CreatePreferenceDTO;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MercadoPagoPaymentGateway implements PaymentGateway {

    @Value("${app.url}")
    private String urlBackend;

    private final MercadoPagoMapper mercadoPagoMapper;
    private final PreferenceClient preferenceClient;
    private final PaymentClient paymentClient;

    public MercadoPagoPaymentGateway(MercadoPagoMapper mercadoPagoMapper, PreferenceClient preferenceClient, PaymentClient paymentClient) {
        this.mercadoPagoMapper = mercadoPagoMapper;
        this.preferenceClient = preferenceClient;
        this.paymentClient = paymentClient;
    }

    @Override
    public ExternalPaymentDetailsDTO getPayment(String externalPaymentId) {
        try {
            Payment paymentMP = paymentClient.get(Long.valueOf(externalPaymentId));
            return mercadoPagoMapper.toExternalPaymentDetailsDTO(paymentMP);
        }
        catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentCreationResultDTO createPayment(CreatePaymentDTO request){

        CreatePreferenceDTO dto = mercadoPagoMapper.toCreatePreferenceDTO(request);

        Preference preference = null;
        try {
            preference = createPreference(dto);
        } catch (MPException | MPApiException e) { // modificar luego
            throw new RuntimeException(e);
        }

        return new PaymentCreationResultDTO(
                preference.getId(), // setteo de preference id
                preference.getInitPoint() // setteo de url checkout
        );

    }

    public Preference createPreference(CreatePreferenceDTO dto) throws MPException, MPApiException {

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(dto.items())
                .externalReference(dto.externalReference())
                .backUrls(dto.backUrls())
                .notificationUrl(urlBackend + "/webhooks/mercadopago")
                .build();

        try {
            return preferenceClient.create(preferenceRequest);
        }
        catch (MPException | MPApiException e) {
            e.printStackTrace(); // eliminar luego
            throw e;
        }
    }
}
