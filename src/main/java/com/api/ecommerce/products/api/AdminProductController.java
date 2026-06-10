package com.api.ecommerce.products.api;

import com.api.ecommerce.products.application.IProductCategoryService;
import com.api.ecommerce.products.application.IProductService;
import com.api.ecommerce.products.domain.ProductCategory;
import com.api.ecommerce.products.dto.request.AdminProductFilter;
import com.api.ecommerce.products.dto.request.CreateProductCategoryDTO;
import com.api.ecommerce.products.dto.request.CreateProductDTO;
import com.api.ecommerce.products.dto.request.EditProductDTO;
import com.api.ecommerce.products.dto.response.AllDataProductDTO;
import com.api.ecommerce.products.infrastructure.persistence.sort.ProductSortBuilder;
import com.api.ecommerce.shared.web.ApiResponse;
import com.api.ecommerce.shared.web.PaginationConstants;
import com.api.ecommerce.users.application.IAppUserService;
import com.api.ecommerce.users.dto.response.UserIdUsernameDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminProductController {

    private IProductService productService;
    private IProductCategoryService productCategoryService;

    public AdminProductController(IProductService productService, IProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@NotBlank @RequestParam("name") String name,
                                           @NotBlank @RequestParam("description") String description,
                                           @NotNull @Positive @RequestParam("price") BigDecimal unitPrice,
                                           @NotNull @PositiveOrZero @RequestParam("stock") Integer stock,
                                           @NotNull @RequestParam("categoryId") Long categoryId,
                                           @NotEmpty @RequestParam("images") List<MultipartFile> images) {
        List<Long> ids = new ArrayList<>();
        ids.add(categoryId);
        CreateProductDTO request = new CreateProductDTO(
                name,
                description,
                unitPrice,
                stock,
                images,
                ids
        );
        productService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-categories")
    public ResponseEntity<Page<ProductCategory>> getCategories(@PageableDefault Pageable pageable){
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize());
        return ResponseEntity.status(HttpStatus.OK)
                .body(productCategoryService.getAll(pageRequest));
    }

    @GetMapping("/get-category-list")
    public ResponseEntity<List<ProductCategory>> getCategoryList(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productCategoryService.getCategoryList());
    }


    @PostMapping("/create-product-category")
    public ResponseEntity<ApiResponse> createProductCategory(@RequestBody CreateProductCategoryDTO request){
        productCategoryService.create(request.getName());
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
            @RequestParam(required = false) String sortByPrice,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean bestSellers,
            @RequestParam(required = false) Boolean hasStock,
            @RequestParam(required = false) String sortByDate,
            @PageableDefault(size = 10,page = 0) Pageable pageable) throws BadRequestException {

        AdminProductFilter adminFilter = new AdminProductFilter(search,categoryIds,active,bestSellers,hasStock);
        Sort sortPageRequest = ProductSortBuilder.build(sortByPrice,bestSellers,sortByDate);
        Pageable pageRequest = PaginationConstants.validatePageable(pageable.getPageNumber(),pageable.getPageSize(),sortPageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllDataProducts(adminFilter,pageRequest));

    }
    @GetMapping("/get-all-data-product/{id}")
    public ResponseEntity<AllDataProductDTO> getAllDataProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllDataProduct(id));
    }

    @PostMapping(value = "/edit-product/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> editProductById(@PathVariable Long id, @ModelAttribute EditProductDTO requestDTO){
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
