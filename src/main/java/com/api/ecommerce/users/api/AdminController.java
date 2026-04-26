package com.api.ecommerce.users.api;

import com.api.ecommerce.products.application.IProductCategoryService;
import com.api.ecommerce.products.application.IProductService;
import com.api.ecommerce.products.dto.request.CreateProductDTO;
import com.api.ecommerce.shared.web.ApiResponse;
import com.api.ecommerce.users.application.IAppUserService;
import com.api.ecommerce.users.dto.response.UserIdUsernameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private IProductService productService;
    private IProductCategoryService productCategoryService;
    private IAppUserService appUserService;

    public AdminController(IProductService productService, IProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody CreateProductDTO requestDTO){
        productService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Product successfully created!"));
    }

    @PostMapping("/create-product-category")
    public ResponseEntity<ApiResponse> createProductCategory(@RequestBody String requestCategory){
        productCategoryService.create(requestCategory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Product successfully created!"));
    }

    @GetMapping("/customer-list")
    public ResponseEntity<Page<UserIdUsernameDTO>> getCustomers(@PageableDefault(size = 10,page = 0) Pageable pageable){
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize());
        return ResponseEntity.status(HttpStatus.OK)
                .body(appUserService.findAllIdAndUsername(pageRequest));
    }
    /*@GetMapping("/product-list")
    public ResponseEntity<Page<ProductSinglePageDTO>> getProducts(@PageableDefault(size = 10,page = 0)Pageable pageable){

    }*/
}
