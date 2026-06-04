package com.api.ecommerce.products.api;

import com.api.ecommerce.products.application.IProductService;
import com.api.ecommerce.products.dto.request.ProductFilter;
import com.api.ecommerce.products.dto.response.AllDataProductDTO;
import com.api.ecommerce.products.dto.response.ProductSinglePageDTO;
import com.api.ecommerce.products.dto.response.PublicProductDTO;
import com.api.ecommerce.products.infrastructure.persistence.sort.ProductSortBuilder;
import com.api.ecommerce.shared.web.ApiResponse;
import com.api.ecommerce.shared.web.PaginationConstants;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/get-single-page/{id}")
    public ResponseEntity<ProductSinglePageDTO> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getSinglePageProductById(id));
    }

    @GetMapping("/get")
    public ResponseEntity<Page<PublicProductDTO>> getPublicProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String sortByPrice,
            @RequestParam(required = false) Boolean bestSellers,
            @RequestParam(required = false) String sortByDate,
            @PageableDefault(size = 9, page = 0) Pageable pageable) throws BadRequestException {
        Sort sortPageRequest = ProductSortBuilder.build(sortByPrice,bestSellers,sortByDate);
        Pageable pageRequest = PaginationConstants.validatePageable(pageable.getPageNumber(),pageable.getPageSize(),sortPageRequest);
        ProductFilter filter = new ProductFilter(search,category);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getPublicProducts(filter,pageRequest));
    }
}
