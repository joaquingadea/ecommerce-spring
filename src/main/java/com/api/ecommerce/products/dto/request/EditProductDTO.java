package com.api.ecommerce.products.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record EditProductDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        List<Long> deleteImages,
        List<MultipartFile> newImages,
        List<Long> categories
) {
}
