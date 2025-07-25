package com.example.product_svc.repository;

import com.example.product_svc.entity.InventoryMovement;
import com.example.product_svc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryMovement, Integer> {
    List<InventoryMovement> findAllByProductVariant(ProductVariant productVariant);
}
