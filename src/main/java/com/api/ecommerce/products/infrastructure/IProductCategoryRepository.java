package com.api.ecommerce.products.infrastructure;

import com.api.ecommerce.products.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
}
