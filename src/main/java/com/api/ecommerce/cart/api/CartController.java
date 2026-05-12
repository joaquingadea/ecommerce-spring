package com.api.ecommerce.cart.api;

import com.api.ecommerce.cart.application.ICartService;
import com.api.ecommerce.cart.dto.response.CartItemDTO;
import com.api.ecommerce.shared.web.ApiResponse;
import com.api.ecommerce.users.infrastructure.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/item/add/{productId}")
    public ResponseEntity<ApiResponse> addItem(@PathVariable Long productId,
                                               @RequestBody Integer quantity,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        cartService.addItem(productId,quantity,userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Product added successfully!"));
    }
    @PatchMapping("/item/increase/{itemId}")
    public ResponseEntity<ApiResponse> increaseItemQuantity(@PathVariable Long itemId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        cartService.increaseItem(itemId,userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item updated successfully!"));
    }
    @PatchMapping("/item/decrease/{itemId}")
    public ResponseEntity<ApiResponse> decreaseItemQuantity(@PathVariable Long itemId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        cartService.decreaseItem(itemId,userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item updated successfully!"));
    }
    @DeleteMapping("/item/delete/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long itemId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        cartService.deleteItem(itemId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item deleted successfully!"));
    }
    @GetMapping("/get")
    public ResponseEntity<List<CartItemDTO>> getCart(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.getCart(userDetails.getId()));
    }
    @GetMapping("/get-total-cart")
    public ResponseEntity<BigDecimal> getTotalCart(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.getTotal(userDetails.getId()));
    }
}
