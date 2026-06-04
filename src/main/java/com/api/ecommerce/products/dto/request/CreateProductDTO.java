package com.api.ecommerce.products.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

public record CreateProductDTO(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @Min(value = 1)
        BigDecimal price,
        @Min(value = 0)
        Integer stock,
        @Size(min = 1, max = 6)
        List<MultipartFile> images,
        @Size(min = 1)
        List<Long> categories
) {
}
