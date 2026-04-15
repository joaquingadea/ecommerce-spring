package com.api.ecommerce.products.application;

import com.api.ecommerce.products.dto.request.CreateProductRequestDTO;

public interface IProductService {
    void create(CreateProductRequestDTO requestDTO);
}
