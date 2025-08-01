package com.example.product_svc.controller;

import com.example.product_svc.dto.CreateProductVariantDto;
import com.example.product_svc.service.ProductVariantService;
import com.example.shared.dto.ProductVariantBasicDto;
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

@RestController
@RequestMapping("/api/v1/product/variant")
public class ProductVariantController {
    @Autowired
    ProductVariantService productVariantService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantBasicDto> getProductVariantById(@PathVariable(value = "id") Integer id) {
        ProductVariantBasicDto variant = productVariantService.getProductVariantById(id);
        return ResponseEntity.ok(variant);
    }

    @PostMapping
    public ResponseEntity<ProductVariantBasicDto> createProductVariant(@Valid @RequestBody CreateProductVariantDto productVariantDto) {
        ProductVariantBasicDto variant = productVariantService.createProductVariant(productVariantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(variant);
    }
}
