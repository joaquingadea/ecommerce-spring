package com.api.ecommerce.products.api;

import com.api.ecommerce.products.application.IProductService;
import com.api.ecommerce.products.dto.request.CreateProductRequestDTO;
import com.api.ecommerce.shared.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/products")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    public ResponseEntity<ApiResponse> create(@RequestBody CreateProductRequestDTO requestDTO){
        productService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Created product successfully!"));
    }
}
