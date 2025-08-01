package com.example.shopping_cart_svc.controller;

import com.example.shared.dto.CreateShoppingCartDto;
import com.example.shopping_cart_svc.dto.CreateCartItemDto;
import com.example.shopping_cart_svc.dto.ShoppingCartDto;
import com.example.shopping_cart_svc.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingService;

    @GetMapping
    public ResponseEntity<List<ShoppingCartDto>> getAllShoppingCarts() {
        List<ShoppingCartDto> carts = shoppingService.getAllShoppingCarts();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUser(@PathVariable(value = "id") UUID id) {
        ShoppingCartDto cart = shoppingService.getShoppingCartByUser(id);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<ShoppingCartDto> createShoppingCart(@Valid @RequestBody CreateShoppingCartDto createCartDto) {
        ShoppingCartDto cart = shoppingService.createCart(createCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PutMapping("/item")
    ResponseEntity<ShoppingCartDto> upsertCartItem(@Valid @RequestBody CreateCartItemDto cartItemDto) {
        ShoppingCartDto cart = shoppingService.upsertCartItem(cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
}
