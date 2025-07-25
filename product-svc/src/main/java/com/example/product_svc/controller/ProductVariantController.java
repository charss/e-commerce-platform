package com.example.product_svc.controller;

import com.example.product_svc.dto.CreateProductVariantDto;
import com.example.product_svc.dto.ProductVariantDto;
import com.example.product_svc.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product/variant")
public class ProductVariantController {
    @Autowired
    ProductVariantService productVariantService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductVariantDto>> getAllProductVariants(@PathVariable(value = "id") UUID id) {
        List<ProductVariantDto> products = productVariantService.getAllVariantsByProduct(id);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductVariantDto> createProductVariant(@Valid @RequestBody CreateProductVariantDto productVariantDto) {
        ProductVariantDto variant = productVariantService.createProductVariant(productVariantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(variant);
    }
}
