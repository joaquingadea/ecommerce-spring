package com.api.ecommerce.cart.api;

import com.api.ecommerce.cart.application.ICartService;
import com.api.ecommerce.cart.dto.request.AddToCartRequestDTO;
import com.api.ecommerce.cart.dto.response.CartItemDTO;
import com.api.ecommerce.shared.security.jwt.JwtPrincipal;
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

    @PostMapping("/item/add-to-cart/{productId}")
    public ResponseEntity<ApiResponse> addItem(@PathVariable Long productId,
                                               @RequestBody AddToCartRequestDTO requestDTO,
                                               @AuthenticationPrincipal JwtPrincipal auth){
        cartService.addToCart(productId,requestDTO.quantity(),auth.userId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Product added successfully!"));
    }
    @PatchMapping("/item/increase/{itemId}")
    public ResponseEntity<ApiResponse> increase(@PathVariable Long itemId, @AuthenticationPrincipal JwtPrincipal auth) {

        cartService.increaseItem(itemId, auth.userId());

        return ResponseEntity.ok(
                new ApiResponse("Quantity increased")
        );
    }
    @PatchMapping("/item/decrease/{itemId}")
    public ResponseEntity<ApiResponse> decreaseItem(@PathVariable Long itemId, @AuthenticationPrincipal JwtPrincipal auth) {

        cartService.decreaseItem(itemId, auth.userId());

        return ResponseEntity.ok(
                new ApiResponse("Quantity decreased")
        );
    }

    @DeleteMapping("/item/delete/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long itemId,
                                                  @AuthenticationPrincipal JwtPrincipal auth){
        cartService.deleteItem(itemId, auth.userId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item deleted successfully!"));
    }
    @GetMapping("/get")
    public ResponseEntity<List<CartItemDTO>> getCart(@AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.getCart(auth.userId()));
    }
    @GetMapping("/get-total-cart")
    public ResponseEntity<BigDecimal> getTotalCart(@AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.getTotal(auth.userId()));
    }
}
