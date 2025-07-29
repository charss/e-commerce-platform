package com.example.shopping_cart_svc.repository;

import com.example.shopping_cart_svc.entity.CartItem;
import com.example.shopping_cart_svc.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByProductVariantIdAndShoppingCart(Integer productVariantId, ShoppingCart shoppingCart);
}
