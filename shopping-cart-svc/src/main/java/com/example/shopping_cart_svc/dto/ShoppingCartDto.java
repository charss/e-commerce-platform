package com.example.shopping_cart_svc.dto;

import com.example.shopping_cart_svc.entity.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ShoppingCartDto(
        Integer id,
        List<CartItemDto> items,
        UUID userId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        OffsetDateTime timestamp
) {
        public static ShoppingCartDto from(ShoppingCart shoppingCart) {
                List<CartItemDto> items = null;
                if (shoppingCart.getItems() != null) {
                        items = shoppingCart.getItems().stream()
                                .map(item -> new CartItemDto(
                                        item.getId(),
                                        item.getShoppingCart().getId(),
                                        item.getProductVariantId(),
                                        item.getSku(),
                                        item.getQuantity()
                                )).toList();
                }

                return new ShoppingCartDto(
                        shoppingCart.getId(),
                        items,
                        shoppingCart.getUserId(),
                        shoppingCart.getTimestamp()
                );
        }
}
