package com.example.product_svc.repository;

import com.example.product_svc.entity.Product;
import com.example.product_svc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    Optional<ProductVariant> findBySku(String sku);
    Boolean existsBySku(String sku);
    List<ProductVariant> findAllByProduct(Product product);
}
