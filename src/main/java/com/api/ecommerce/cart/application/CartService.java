package com.api.ecommerce.cart.application;

import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService{
    
    @Override
    public void addItem(Long productId,Integer quantity,Long userId) {
        
    }
}
