package com.example.product_svc.repository;

import com.example.product_svc.entity.ProductVariant;
import com.example.product_svc.entity.ProductVariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantAttributeRepository extends JpaRepository<ProductVariantAttribute, Integer> {
    List<ProductVariantAttribute> findAllByProductVariant(ProductVariant productVariant);
}
