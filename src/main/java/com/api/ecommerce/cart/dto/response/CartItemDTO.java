package com.api.ecommerce.cart.dto.response;

import java.math.BigDecimal;

public record CartItemDTO(
        Long itemId,
        Integer quantity,

        Long productId,
        String productName,
        Integer stock,
        BigDecimal unitPrice
) {
}
