package com.api.ecommerce.products.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record ProductFilter(
        List<Long> categoryIds,
        BigDecimal minPrice
) {
}
