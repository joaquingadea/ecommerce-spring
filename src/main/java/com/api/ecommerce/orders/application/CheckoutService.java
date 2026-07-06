package com.api.ecommerce.orders.application;

import com.api.ecommerce.cart.application.ICartService;
import com.api.ecommerce.cart.domain.Cart;
import com.api.ecommerce.cart.infrastructure.ICartRepository;
import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.orders.dto.response.PaymentCheckoutDTO;
import com.api.ecommerce.orders.dto.response.StartCheckoutDTO;
import com.api.ecommerce.payments.application.IPaymentService;
import com.api.ecommerce.payments.domain.Payment;
import com.api.ecommerce.payments.domain.PaymentGateway;
import com.api.ecommerce.payments.dto.request.CreatePaymentDTO;
import com.api.ecommerce.payments.dto.request.PaymentItemDTO;
import com.api.ecommerce.payments.dto.response.PaymentCreationResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CheckoutService {

    @Value("${app.url.frontend.ecommerce}")
    private String urlFrontEcommerce;

    private final PaymentGateway paymentGateway;
    private final IPaymentService paymentService;
    private final ICartRepository cartRepository;
    private final IOrderService orderService;
    private final ICartService cartService;

    public CheckoutService(PaymentGateway paymentGateway, IPaymentService paymentService, ICartRepository cartRepository, IOrderService orderService, ICartService cartService) {
        this.paymentGateway = paymentGateway;
        this.paymentService = paymentService;
        this.cartRepository = cartRepository;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    public StartCheckoutDTO startCheckout(Long userId) {
        record CheckoutOrderDetailDTO(String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal){}

        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        cartService.validateCart(cart);
        Order order = orderService.createOrder(userId, cart);

        return new StartCheckoutDTO(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getOrderDetails().stream()
                        .map(orderDetail ->
                                new CheckoutOrderDetailDTO(
                                        orderDetail.getProduct().getName(),
                                        orderDetail.getQuantity(),
                                        orderDetail.getUnitPrice(),
                                        orderDetail.getSubtotal()
                                )).toList()
        );
    }

    public PaymentCheckoutDTO payOrder(Long orderId, Long userId){
        Order pendingOrder = orderService.getPendingOrder(orderId,userId);
        // se crea el intento de pago o se obtiene el pendiente (se tira una excepcion si esta aprobado)
        Payment payment = paymentService.getOrCreateAttemptPayment(pendingOrder);
        // payment de MP
        CreatePaymentDTO requestDTO = new CreatePaymentDTO(
                payment.getId(),
                pendingOrder.getOrderDetails().stream().map(orderDetail -> new PaymentItemDTO(
                        orderDetail.getId().toString(),
                        orderDetail.getProduct().getName(),
                        orderDetail.getProduct().getDescription(),
                        orderDetail.getQuantity(),
                        orderDetail.getProduct().getImages().get(0).getUrl(),
                        orderDetail.getUnitPrice()
                )).toList(),
                urlFrontEcommerce + "/payment-success.html",
                urlFrontEcommerce + "/payment-pending.html",
                urlFrontEcommerce + "/payment-failure.html"
        );

        PaymentCreationResultDTO paymentResultDTO = paymentGateway.createPayment(requestDTO);

        paymentService.attachPreferenceId(payment, paymentResultDTO.preferenceId());
        return new PaymentCheckoutDTO(paymentResultDTO.checkoutUrl());
    }
}
