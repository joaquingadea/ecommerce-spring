package com.api.ecommerce.products.infrastructure;

import com.api.ecommerce.products.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage,Long> {
}
