package com.api.ecommerce.cart.api;

import com.api.ecommerce.cart.application.ICartService;
import com.api.ecommerce.shared.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-item/{productId}")
    public ResponseEntity<ApiResponse> addItem(@PathVariable Long productId, @RequestBody Integer quantity, Authentication authentication){
        cartService.addItem(productId,quantity,null); // sacado del JWT
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Product added successfully!"));
    }
}
