package com.api.ecommerce.products.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record EditProductDTO(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @Min(value = 1)
        BigDecimal unitPrice,
        @Min(value = 0)
        Integer stock,
        List<Long> deleteImages,
        List<MultipartFile> newImages,
        @Size(min = 1)
        List<Long> categories
) {
}
