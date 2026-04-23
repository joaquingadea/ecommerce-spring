package com.api.ecommerce.products.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductSinglePageDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Integer stock,
        List<String> categories,
        List<String> images
) {
}
