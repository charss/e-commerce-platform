package com.example.shopping_cart_svc.service;

import com.example.shared.dto.CreateShoppingCartDto;
import com.example.shared.dto.ProductVariantBasicDto;
import com.example.shared.exception.ObjectNotFoundException;
import com.example.shopping_cart_svc.client.ProductSvcClient;
import com.example.shopping_cart_svc.dto.CreateCartItemDto;
import com.example.shopping_cart_svc.dto.ShoppingCartDto;
import com.example.shopping_cart_svc.entity.CartItem;
import com.example.shopping_cart_svc.entity.ShoppingCart;
import com.example.shopping_cart_svc.exception.InvalidUpsertException;
import com.example.shopping_cart_svc.exception.UserCartAlreadyExistsException;
import com.example.shopping_cart_svc.exception.UserCartNotFoundException;
import com.example.shopping_cart_svc.repository.CartItemRepository;
import com.example.shopping_cart_svc.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartService {
    @Autowired
    ShoppingCartRepository shoppingRepo;
    @Autowired
    CartItemRepository cartItemRepo;
    @Autowired
    private ProductSvcClient productSvcClient;

    public List<ShoppingCartDto> getAllShoppingCarts() {
        return this.shoppingRepo.findAll().stream()
                .map(ShoppingCartDto::from)
                .toList();
    }

    public ShoppingCartDto getShoppingCartByUser(UUID id) {
        ShoppingCart cart = shoppingRepo.findByUserId(id).orElseThrow(
                () -> new UserCartNotFoundException("Cart of User " + id + " not found."));

        return ShoppingCartDto.from(cart);
    }

    @Transactional
    public ShoppingCartDto createCart(CreateShoppingCartDto cartDto) {
        if (shoppingRepo.findByUserId(cartDto.userId()).isPresent()) {
            throw new UserCartAlreadyExistsException("Cart of User " + cartDto.userId() + " already exists.");
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(cartDto.userId());

        return ShoppingCartDto.from(shoppingRepo.save(cart));
    }

    @Transactional
    public ShoppingCartDto upsertCartItem(CreateCartItemDto cartItemDto) {
        ShoppingCart cart = shoppingRepo.findById(cartItemDto.shoppingCartId()).orElseThrow(
                () -> new ObjectNotFoundException("Shopping Cart", cartItemDto.shoppingCartId()));

        ProductVariantBasicDto product = productSvcClient.getProductVariantById(cartItemDto.productVariantId());

        if (cartItemDto.quantity() < 0) throw new InvalidUpsertException("Quantity cannot be negative");
        if (cartItemDto.quantity() > product.stock())
            throw new InvalidUpsertException("Invalid upsert request. Requested quantity (" + cartItemDto.quantity() + ") exceeds available stock (" + product.stock() + ")");


        CartItem item = cartItemRepo.findByProductVariantIdAndShoppingCart(cartItemDto.productVariantId(), cart)
                .orElse(null);
        if (item == null && cartItemDto.quantity() == 0) {
            throw new InvalidUpsertException("Invalid upsert request. New item cannot have 0 quantity");
        }

        if (item == null) {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartItem.setProductVariantId(cartItemDto.productVariantId());
            cartItem.setSku(product.sku());
            cartItem.setQuantity(cartItemDto.quantity());
            cartItemRepo.save(cartItem);
        } else if (cartItemDto.quantity() == 0) {
            cartItemRepo.delete(item);
            cart.getItems().remove(item);
        } else {
            item.setQuantity(cartItemDto.quantity());
        }

        return ShoppingCartDto.from(cart);
    }
}
