package com.api.ecommerce.products.application;

import com.api.ecommerce.products.domain.Product;
import com.api.ecommerce.products.domain.ProductCategory;
import com.api.ecommerce.products.domain.ProductImage;
import com.api.ecommerce.products.domain.ProductStatus;
import com.api.ecommerce.products.dto.request.AdminProductFilter;
import com.api.ecommerce.products.dto.request.CreateProductDTO;
import com.api.ecommerce.products.dto.request.EditProductDTO;
import com.api.ecommerce.products.dto.request.ProductFilter;
import com.api.ecommerce.products.dto.response.AllDataProductDTO;
import com.api.ecommerce.products.dto.response.ProductSinglePageDTO;
import com.api.ecommerce.products.dto.response.PublicProductDTO;
import com.api.ecommerce.products.infrastructure.persistence.IProductCategoryRepository;
import com.api.ecommerce.products.infrastructure.persistence.IProductImageRepository;
import com.api.ecommerce.products.infrastructure.persistence.IProductRepository;
import com.api.ecommerce.products.infrastructure.persistence.specification.AdminProductSpecBuilder;
import com.api.ecommerce.products.infrastructure.persistence.specification.ProductSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService{

    @Value("app.url")
    private String urlApp;

    private IProductRepository productRepository;
    private FileStorageService fileStorageService;
    private IProductCategoryRepository categoryRepository;
    private IProductImageRepository imageRepository;

    public ProductService(IProductRepository productRepository, FileStorageService fileStorageService, IProductCategoryRepository categoryRepository, IProductImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.fileStorageService = fileStorageService;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    public ProductImage mapToImage(MultipartFile file, Product newProduct){
        return new ProductImage(1L, fileStorageService.saveFile(file),newProduct);
    }

    @Override
    public void create(CreateProductDTO requestDTO) {
        Product newProduct = new Product(
                1L,
                requestDTO.name(),
                requestDTO.description(),
                requestDTO.stock(),
                requestDTO.price(),
                0,
                null,
                null,
                ProductStatus.ACTIVE
        );

        List<ProductImage> images = requestDTO.images()
                .stream().map(file -> mapToImage(file,newProduct))
                .toList();

        List<ProductCategory> categories =
                new ArrayList<>(categoryRepository.findAllById(requestDTO.categories()));

        newProduct.setImages(images);
        newProduct.setProductCategories(categories);

        productRepository.save(newProduct);
    }

    record longAndString(Long id, String text){}
    @Override
    public ProductSinglePageDTO getSinglePageProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductSinglePageDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getUnitPrice(),
                        product.getStock(),
                        product.getProductCategories().stream()
                                .map(category -> new longAndString(category.getId(),category.getName())).toList(),
                        product.getImages().stream()
                                .map(image -> new longAndString(image.getId(), urlApp + image.getUrl())).toList()
                ))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Page<PublicProductDTO> getPublicProducts(ProductFilter filter, Pageable pageRequest) {
        Specification<Product> spec = ProductSpecificationBuilder.build(filter);
        return productRepository.findBy(
                spec,query ->
                query.as(PublicProductDTO.class)
                        .page(pageRequest)
        );
    }

    @Override
    public Page<AllDataProductDTO> getAllDataProducts(AdminProductFilter filter, Pageable pageRequest) {
        Specification<Product> spec = AdminProductSpecBuilder.build(filter);
        return productRepository.findBy(spec,query ->
                    query.as(AllDataProductDTO.class)
                            .page(pageRequest)
                );
    }

    @Override
    public void editById(Long id, EditProductDTO requestDTO) {
        Product productRepo = productRepository.findById(id).orElseThrow();
        productRepo.setName(requestDTO.name());
        productRepo.setDescription(requestDTO.description());
        productRepo.setUnitPrice(requestDTO.price());
        productRepo.setStock(requestDTO.stock());

        List<ProductCategory> categories = categoryRepository.findAllById(requestDTO.categories());
        if (categories.size() != requestDTO.categories().size()) {
            throw new EntityNotFoundException("Category not found");
        }

        productRepo.setProductCategories(categories);

        if (requestDTO.deleteImages() != null && !requestDTO.deleteImages().isEmpty()){
            productRepo.getImages().removeIf((image) -> {
                   boolean shouldDelete = requestDTO.deleteImages().contains(image.getId());
                   if(shouldDelete){
                        fileStorageService.deleteImage(image.getUrl());
                   }
                   return shouldDelete;
            });
        }
        if (requestDTO.newImages() != null && !requestDTO.newImages().isEmpty()){
            for (MultipartFile file : requestDTO.newImages()) {
                String savedPath = fileStorageService.saveFile(file);

                ProductImage image = new ProductImage();
                image.setUrl(savedPath);
                image.setProduct(productRepo);

                productRepo.getImages().add(image);
            }
        }

        productRepository.save(productRepo);
    }

    @Override
    public void deactivate(Long id) {
        Product deactivateProduct = productRepository.findById(id).orElseThrow();
        deactivateProduct.setStatus(ProductStatus.INACTIVE);
    }

    @Override
    public void activate(Long id) {
        Product activateProduct = productRepository.findById(id).orElseThrow();
        activateProduct.setStatus(ProductStatus.ACTIVE);
    }


}
