package com.api.ecommerce.products.api;

import com.api.ecommerce.products.application.IProductCategoryService;
import com.api.ecommerce.products.application.IProductService;
import com.api.ecommerce.products.dto.request.AdminProductFilter;
import com.api.ecommerce.products.dto.request.CreateProductDTO;
import com.api.ecommerce.products.dto.request.EditProductDTO;
import com.api.ecommerce.products.dto.response.AllDataProductDTO;
import com.api.ecommerce.products.infrastructure.persistence.sort.ProductSortBuilder;
import com.api.ecommerce.shared.web.ApiResponse;
import com.api.ecommerce.shared.web.PaginationConstants;
import com.api.ecommerce.users.application.IAppUserService;
import com.api.ecommerce.users.dto.response.UserIdUsernameDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminProductController {

    private IProductService productService;
    private IProductCategoryService productCategoryService;

    public AdminProductController(IProductService productService, IProductCategoryService productCategoryService) {
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
                .body(new ApiResponse("Product category successfully created!"));
    }
    @PatchMapping("/edit-product-category/{id}")
    public ResponseEntity<ApiResponse> editProductCategory(@PathVariable Long id,@RequestBody String newName){
        productCategoryService.edit(id,newName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Product category successfully edited!"));
    }


    @GetMapping("/product-list")
    public ResponseEntity<Page<AllDataProductDTO>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean bestSellers,
            @RequestParam(required = false) Boolean hasStock,
            @PageableDefault(size = 10,page = 0) Pageable pageable) throws BadRequestException {

        AdminProductFilter adminFilter = new AdminProductFilter(search,categoryIds,active,bestSellers,hasStock);
        Sort sortPageRequest = ProductSortBuilder.build(sort,bestSellers);
        Pageable pageRequest = PaginationConstants.validatePageable(pageable.getPageNumber(),pageable.getPageSize(),sortPageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllDataProducts(adminFilter,pageRequest));

    }
    @PutMapping("/edit-product/{id}")
    public ResponseEntity<ApiResponse> editProductById(@PathVariable Long id, @RequestBody EditProductDTO requestDTO){
        productService.editById(id,requestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Product successfully edited!"));
    }
    @PatchMapping("/deactivate-product/{id}")
    public ResponseEntity<ApiResponse> deactivateProduct(@PathVariable Long id){
        productService.deactivate(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Product successfully deactivated!"));
    }
    @PatchMapping("/activate-product/{id}")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable Long id){
        productService.activate(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Product successfully deactivated!"));
    }
}
