package com.api.ecommerce.products.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal unitPrice;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private Set<ProductImage> images;
    @ManyToMany
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<ProductCategory> productCategories;
}
