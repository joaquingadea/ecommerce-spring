package com.api.ecommerce.products.dto.request;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.Set;

public record CreateProductRequestDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Set<MultipartFile> images,
        Set<Long> categories
) {
}
