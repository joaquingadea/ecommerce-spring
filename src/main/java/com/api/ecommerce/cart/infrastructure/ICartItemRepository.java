package com.api.ecommerce.cart.infrastructure;

import com.api.ecommerce.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {
}
