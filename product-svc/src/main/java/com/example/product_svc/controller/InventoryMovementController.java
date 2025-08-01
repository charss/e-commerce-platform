package com.example.product_svc.controller;

import com.example.product_svc.dto.InventoryMovementDto;
import com.example.product_svc.service.InventoryMovementService;
import com.example.shared.dto.BulkCreateInventoryMovementDto;
import com.example.shared.dto.CreateInventoryMovementDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryMovementController {
    @Autowired
    InventoryMovementService inventoryMovementService;

    @GetMapping
    public ResponseEntity<List<InventoryMovementDto>> getAllInventoryMovement() {
        List<InventoryMovementDto> movements = inventoryMovementService.getAllInventoryMovements();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/variant/{id}")
    public ResponseEntity<List<InventoryMovementDto>> getAllInventoryMovementByVariant(@PathVariable(value = "id") Integer id) {
        List<InventoryMovementDto> movements = inventoryMovementService.getAllInventoryMovementsByVariant(id);
        return ResponseEntity.ok(movements);
    }

    @PostMapping
    public ResponseEntity<InventoryMovementDto> createInventoryMovement(@Valid @RequestBody CreateInventoryMovementDto createDto) {
        InventoryMovementDto movement = inventoryMovementService.processMovement(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @PostMapping("bulk")
    public ResponseEntity<Void> createBulkInventoryMovement(@Valid @RequestBody BulkCreateInventoryMovementDto bulkDto) {
        inventoryMovementService.processBulkMovement(bulkDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteInventoryMovement(@PathVariable(value = "id") Integer id) {
        inventoryMovementService.deleteInventoryMovement(id);
    }
}
