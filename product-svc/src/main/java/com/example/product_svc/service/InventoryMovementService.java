package com.example.product_svc.service;

import com.example.product_svc.common.MovementType;
import com.example.product_svc.dto.CreateInventoryMovementDto;
import com.example.product_svc.dto.InventoryMovementDto;
import com.example.product_svc.entity.InventoryMovement;
import com.example.product_svc.entity.ProductVariant;
import com.example.product_svc.exception.ObjectNotFoundException;
import com.example.product_svc.repository.InventoryRepository;
import com.example.product_svc.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InventoryMovementService {
    @Autowired
    InventoryRepository inventoryRepo;
    @Autowired
    ProductVariantRepository productVariantRepo;
    @Autowired
    ProductVariantService productVariantService;

    public List<InventoryMovementDto> getAllInventoryMovements() {
        return inventoryRepo.findAll().stream()
                .map(InventoryMovementDto::from).toList();
    }

    public List<InventoryMovementDto> getAllInventoryMovementsByVariant(Integer id) {
        ProductVariant productVariant = productVariantRepo.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", id));

        return inventoryRepo.findAllByProductVariant(productVariant).stream()
                .map(InventoryMovementDto::from).toList();
    }

    @Transactional
    public InventoryMovementDto createInventoryMovement(CreateInventoryMovementDto inventoryDto) {
        ProductVariant productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));

        InventoryMovement inventoryMovement = buildInventoryMovement(inventoryDto, productVariant);
        InventoryMovement saveInventory = inventoryRepo.saveAndFlush(inventoryMovement);
        productVariantService.updateStock(productVariant, inventoryDto.movementType(), inventoryDto.quantity());
        return InventoryMovementDto.from(saveInventory);
    }

    private InventoryMovement buildInventoryMovement(CreateInventoryMovementDto inventoryDto, ProductVariant productVariant) {
        InventoryMovement inventoryMovement = new InventoryMovement();
        inventoryMovement.setProductVariant(productVariant);
        inventoryMovement.setQuantity(inventoryDto.quantity());
        inventoryMovement.setMovementType(inventoryDto.movementType().name());
        return inventoryMovement;
    }

    @Transactional
    public InventoryMovementDto updateInventoryMovement(Integer inventoryId, CreateInventoryMovementDto inventoryDto) {
        InventoryMovement inventoryMovement = inventoryRepo.findById(inventoryId).orElseThrow(
                () -> new ObjectNotFoundException("Inventory Movement", inventoryId));

        ProductVariant productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));

        productVariantService.reverseStock(productVariant, MovementType.valueOf(inventoryMovement.getMovementType()), inventoryMovement.getQuantity());
        inventoryMovement.setProductVariant(productVariant);
        inventoryMovement.setQuantity(inventoryDto.quantity());
        inventoryMovement.setMovementType(inventoryDto.movementType().name());
        InventoryMovement saveInventory = inventoryRepo.saveAndFlush(inventoryMovement);
        productVariantService.updateStock(productVariant, inventoryDto.movementType(), inventoryDto.quantity());

        return InventoryMovementDto.from(saveInventory);
    }

    @Transactional
    public void deleteInventoryMovement(Integer inventoryId) {
        InventoryMovement inventoryMovement = inventoryRepo.findById(inventoryId).orElseThrow(
                () -> new ObjectNotFoundException("Inventory Movement", inventoryId));
        productVariantService.reverseStock(inventoryMovement.getProductVariant(), MovementType.valueOf(inventoryMovement.getMovementType()), inventoryMovement.getQuantity());
        inventoryRepo.delete(inventoryMovement);
    }
}
