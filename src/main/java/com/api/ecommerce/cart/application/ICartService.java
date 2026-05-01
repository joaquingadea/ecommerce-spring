package com.api.ecommerce.cart.application;

public interface ICartService {
    void addItem(Long productId,Integer quantity, Long userId);
}
