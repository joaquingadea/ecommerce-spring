package com.api.ecommerce.products.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductCategoryDTO {
    @NotEmpty
    private String name;
}
