package com.shop.service;

import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;

    public Cart getOrCreateCart(User user){
        return cartRepository.findByUserId(user.getId()).orElseGet(()->{
                Cart c = new Cart(); c.setUser(user); return cartRepository.save(c);});
    }
    public List<CartItem> items(Cart cart){
        return cartItemRepository.findByCartId(cart.getId());
    }
    @Transactional
    public CartItem addToCart(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productService.getProduct(productId);



        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;



            item.setQuantity(newQuantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            return cartItemRepository.save(newItem);
        }
    }
    @Transactional                 // <- ensures changes are flushed/committed
    public void updateQuantity(Long cartItemId, int qty) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found: " + cartItemId));

        // set the new quantity on the managed entity
        item.setQuantity(qty);

        // Optional but explicit — persist change immediately
        cartItemRepository.save(item);
        // you can also call cartItemRepository.flush() if you want to force an immediate DB write
    }
    public List<CartItem> getCartItems(User user){
         Cart cart = cartRepository.findByUser(user).orElse(null);
         if (cart != null){
             return cartItemRepository.findByCartId(cart.getId());
         }
         return List.of();
    }
    @Transactional
    public void clearCart(User user){
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if(cart == null) return;

        cartItemRepository.deleteByCart(cart);
    }
    @Transactional
    public void remove(Long itemId){ cartItemRepository.deleteById(itemId); }
}
