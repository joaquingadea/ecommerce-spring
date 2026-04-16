package com.api.ecommerce.cart.infrastructure;

import com.api.ecommerce.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Long> {
}
