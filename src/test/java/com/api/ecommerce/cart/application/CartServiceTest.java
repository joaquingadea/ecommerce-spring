package com.api.ecommerce.cart.application;

import com.api.ecommerce.cart.domain.Cart;
import com.api.ecommerce.cart.domain.CartItem;
import com.api.ecommerce.cart.infrastructure.ICartItemRepository;
import com.api.ecommerce.products.domain.Product;
import com.api.ecommerce.products.infrastructure.persistence.IProductRepository;
import com.api.ecommerce.users.domain.AppUser;
import com.api.ecommerce.users.infrastructure.persistence.IAppUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private IAppUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private ICartItemRepository cartItemRepository;
    @InjectMocks
    private CartService cartService;

    @Nested
    class GetOrCreateCartItemTests{
        @Test
        void shouldReturnExistingCartItemWhenAlreadyExists(){
            Product product = new Product();
            product.setId(1L);

            Cart cart = new Cart();
            CartItem existing = new CartItem(1L,5,cart,product);

            cart.setItems(new HashSet<>(List.of(existing)));

            CartItem result = cartService.getOrCreateCartItem(cart,product);
            assertEquals(existing,result);
        }
        @Test
        void shouldCreateAndReturnCartItemWhenDoesNotExists(){
            Product product = new Product();
            Cart cart = new Cart();

            cart.setItems(new HashSet<>(List.of()));

            CartItem result = cartService.getOrCreateCartItem(cart,product);

            assertEquals(0,result.getQuantity());
            assertEquals(cart,result.getCart());
            assertEquals(product,result.getProduct());
        }
    }

    @Nested
    class UpdateCartItemQuantityTests{
        @Test
        void shouldThrowExceptionWhenUserDoesNotOwnCart(){
            CartItem item = new CartItem();
            Cart cart = new Cart(null,null, new AppUser());
            item.setCart(cart);

            AppUser user = new AppUser();
            user.setId(2L);

            item.getCart().getUser().setId(1L);

            assertThrows(RuntimeException.class,() -> {
                cartService.updateCartItemQuantity(item,1, user.getId());
            });
        }
        @Test
        void shouldThrowExceptionWhenNewQuantityIsInvalid(){
            CartItem item = new CartItem();
            Cart cart = new Cart(null,null, new AppUser());
            item.setCart(cart);
            AppUser user = new AppUser();
            user.setId(1L);
            item.getCart().getUser().setId(1L);

            assertThrows(RuntimeException.class,() -> {
                cartService.updateCartItemQuantity(item,-1, user.getId());
            });
        }
    }

}

