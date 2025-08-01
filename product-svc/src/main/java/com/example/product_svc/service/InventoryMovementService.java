package com.example.product_svc.service;

import com.example.product_svc.dto.InventoryMovementDto;
import com.example.product_svc.entity.InventoryMovement;
import com.example.product_svc.entity.ProductVariant;
import com.example.product_svc.exception.InvalidMovementSourceException;
import com.example.product_svc.exception.InvalidMovementTypeException;
import com.example.product_svc.repository.InventoryRepository;
import com.example.product_svc.repository.ProductVariantRepository;
import com.example.shared.dto.BulkCreateInventoryMovementDto;
import com.example.shared.dto.CreateInventoryMovementDto;
import com.example.shared.exception.ObjectNotFoundException;
import jakarta.annotation.Nullable;
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

    public void processBulkMovement(BulkCreateInventoryMovementDto bulkDto) {
        bulkDto.movements().forEach(this::processMovement);
    }

    public InventoryMovementDto processMovement(CreateInventoryMovementDto dto) {
        return switch (dto.sourceType()) {
            case ORDER -> processOrderMovement(dto);
            case RESTOCK -> addStockFromRestock(dto);
//            case RETURN -> addStockFromReturn(dto);
            default -> throw new InvalidMovementSourceException("Unsupported source type: " + dto.sourceType());
        };
    }

    private InventoryMovementDto processOrderMovement(CreateInventoryMovementDto dto) {
        return switch (dto.movementType()) {
            case RESERVE -> reserveInventoryForOrder(dto);
            case OUT -> shipReservedInventory(dto);
            case ORDER_CANCELLED -> cancelReservedInventory(dto);
            default -> throw new InvalidMovementTypeException("Unsupported movement type: " + dto.movementType());
        };
    }

    @Transactional
    public InventoryMovementDto addStockFromRestock(CreateInventoryMovementDto inventoryDto) {
        ProductVariant productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));

        InventoryMovement inventoryMovement = buildInventoryMovement(inventoryDto, productVariant);
        InventoryMovement saveInventory = inventoryRepo.saveAndFlush(inventoryMovement);
        productVariantService.updateStock(productVariant, inventoryDto.movementType(), inventoryDto.quantity());
        return InventoryMovementDto.from(saveInventory);
    }

    @Transactional
    public InventoryMovementDto reserveInventoryForOrder(CreateInventoryMovementDto inventoryDto) {
        ProductVariant productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));

        InventoryMovement saveInventory = inventoryRepo.saveAndFlush(buildInventoryMovement(inventoryDto, productVariant));
        productVariantService.updateStock(productVariant, inventoryDto.movementType(), inventoryDto.quantity());
        return InventoryMovementDto.from(saveInventory);
    }

    @Transactional
    public InventoryMovementDto shipReservedInventory(CreateInventoryMovementDto inventoryDto) {
        InventoryMovement inventoryMovement = buildInventoryMovement(inventoryDto, null);
        InventoryMovement saveInventory = inventoryRepo.save(inventoryMovement);
        return InventoryMovementDto.from(saveInventory);
    }

    @Transactional
    public InventoryMovementDto cancelReservedInventory(CreateInventoryMovementDto inventoryDto) {
        ProductVariant productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));
        InventoryMovement saveInventory = inventoryRepo.saveAndFlush(buildInventoryMovement(inventoryDto, productVariant));
        productVariantService.updateStock(productVariant, inventoryDto.movementType(), inventoryDto.quantity());
        return InventoryMovementDto.from(saveInventory);
    }

    private InventoryMovement buildInventoryMovement(CreateInventoryMovementDto inventoryDto, @Nullable ProductVariant productVariant) {
        if (productVariant == null) {
            productVariant = productVariantRepo.findById(inventoryDto.variantId()).orElseThrow(
                    () -> new ObjectNotFoundException("Product Variant", inventoryDto.variantId()));
        }

        InventoryMovement inventoryMovement = new InventoryMovement();
        inventoryMovement.setProductVariant(productVariant);
        inventoryMovement.setQuantity(inventoryDto.quantity());
        inventoryMovement.setMovementType(inventoryDto.movementType());
        inventoryMovement.setSourceType(inventoryDto.sourceType());
        inventoryMovement.setSourceId(inventoryDto.sourceId().orElse(null));
        return inventoryMovement;
    }

    @Transactional
    public void deleteInventoryMovement(Integer inventoryId) {
        InventoryMovement inventoryMovement = inventoryRepo.findById(inventoryId).orElseThrow(
                () -> new ObjectNotFoundException("Inventory Movement", inventoryId));
        productVariantService.reverseStock(inventoryMovement.getProductVariant(), inventoryMovement.getMovementType(), inventoryMovement.getQuantity());
        inventoryRepo.delete(inventoryMovement);
    }
}
