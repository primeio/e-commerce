package com.shop.repository;

import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
   List<CartItem> findByCartId(Long cartId);

   Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

   @Transactional
    @Modifying
   @Query("delete from CartItem c where c.cart = :cart")
   void deleteByCart(@Param("cart") Cart cart);

}
