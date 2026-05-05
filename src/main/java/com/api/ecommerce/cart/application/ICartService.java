package com.api.ecommerce.cart.application;

import com.api.ecommerce.cart.dto.response.CartItemDTO;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface ICartService {
    void addItem(Long productId,Integer quantity, Long userId);
    void increaseItem(Long itemId, Long userId);
    void decreaseItem(Long itemId, Long userId);
    List<CartItemDTO> getCart(Long userId);
    BigDecimal getTotal(Long userId);
    void deleteItem(Long itemId,Long userId);
}
