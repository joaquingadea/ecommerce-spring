package com.api.ecommerce.orders.dto.response;

import com.api.ecommerce.orders.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class StartCheckoutDTO {
    private Long orderId;
    private BigDecimal total;
    private OrderStatus status;
    private List<?> orderDetails;
}