package com.api.ecommerce.products.application;

import com.api.ecommerce.products.dto.request.AdminProductFilter;
import com.api.ecommerce.products.dto.request.CreateProductDTO;
import com.api.ecommerce.products.dto.request.EditProductDTO;
import com.api.ecommerce.products.dto.request.ProductFilter;
import com.api.ecommerce.products.dto.response.AllDataProductDTO;
import com.api.ecommerce.products.dto.response.ProductSinglePageDTO;
import com.api.ecommerce.products.dto.response.PublicProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    void create(CreateProductDTO requestDTO);
    ProductSinglePageDTO getSinglePageProductById(Long id);
    Page<PublicProductDTO> getPublicProducts(ProductFilter filter, Pageable pageRequest);
    Page<AllDataProductDTO> getAllDataProducts(AdminProductFilter filter, Pageable pageRequest);
    void editById(Long id, EditProductDTO requestDTO);
    void deactivate(Long id);
    void activate(Long id);
}
