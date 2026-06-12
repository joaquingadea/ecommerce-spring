package com.api.ecommerce.cart.application;

import com.api.ecommerce.cart.domain.Cart;
import com.api.ecommerce.cart.domain.CartItem;
import com.api.ecommerce.cart.dto.response.CartItemDTO;
import com.api.ecommerce.cart.infrastructure.ICartItemRepository;
import com.api.ecommerce.cart.infrastructure.ICartRepository;
import com.api.ecommerce.products.domain.Product;
import com.api.ecommerce.products.infrastructure.persistence.IProductRepository;
import com.api.ecommerce.users.domain.AppUser;
import com.api.ecommerce.users.infrastructure.persistence.IAppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartService implements ICartService{

    private IAppUserRepository userRepository;
    private IProductRepository productRepository;
    private ICartItemRepository cartItemRepository;

    public CartService(IAppUserRepository userRepository, IProductRepository productRepository, ICartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem getOrCreateCartItem(Cart cart, Product product){
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(null,0,cart,product);
                    cart.getItems().add(newItem);
                    return newItem;
                });
    }

    // Delta en matematica representa una cambio finito, incremento o diferencia en una variable
    public void updateCartItemQuantity(CartItem item, Integer newQuantity,Long userId) {
        if (!item.getCart().getUser().getId().equals(userId)){
            throw new RuntimeException("Unauthorized!");
        }
        if (newQuantity == 0){
            Cart cart = item.getCart();
            cart.getItems().remove(item);
            return;
        }
        if(newQuantity > 0 && newQuantity <= item.getProduct().getStock()){
            item.setQuantity(newQuantity);
        }
        else {
            throw new RuntimeException("Invalid quantity change!");
        }
    }

    @Override
    public void addToCart(Long productId,Integer quantity,Long userId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Cart userCart = userRepository.findById(userId).orElseThrow().getUserCart();
        CartItem item = getOrCreateCartItem(userCart,product);

        if (quantity < 1 || quantity > product.getStock()) {
            throw new RuntimeException("Invalid quantity!");
        }
        // si el usuario ya tenia almacenado en el carrito una cantidad, esto no lo sobrescribe
        Integer newQuantity = quantity + item.getQuantity();
        if (newQuantity > product.getStock()){
            throw new RuntimeException("Invalid quantity!");
        }

        updateCartItemQuantity(item,newQuantity,userId);
    }

    @Override
    public void increaseItem(Long itemId, Long userId) {

        CartItem item =
                cartItemRepository.findById(itemId)
                        .orElseThrow();

        Integer newQuantity =
                item.getQuantity() + 1;

        updateCartItemQuantity(
                item,
                newQuantity,
                userId
        );
    }
    @Override
    public void decreaseItem(Long itemId, Long userId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow();

        Integer newQuantity = item.getQuantity() - 1;

        updateCartItemQuantity(item,newQuantity,userId);
    }

    @Override
    public List<CartItemDTO> getCart(Long userId) {
        return userRepository.findById(userId).orElseThrow().getUserCart().getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getStock(),
                        item.getProduct().getUnitPrice()
                )).toList();
    }

    @Override
    public BigDecimal getTotal(Long userId) {

        AppUser user = userRepository.findById(userId).orElseThrow();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : user.getUserCart().getItems()) {

            total = total.add(
                    item.getProduct()
                            .getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        return total;
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if (!item.getCart().getUser().getId().equals(userId)){
            throw new RuntimeException("Unauthorized!");
        }
        Cart cart = item.getCart();
        cart.getItems().remove(item);
    }


}
