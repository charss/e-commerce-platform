package com.example.product_svc.controller;

import com.example.product_svc.dto.CreateProductAttributeDto;
import com.example.product_svc.dto.ProductAttributeDto;
import com.example.product_svc.service.ProductAttributeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/attribute")
public class ProductAttributeController {
    @Autowired
    ProductAttributeService productAttribService;

    @GetMapping
    public ResponseEntity<List<ProductAttributeDto>> getAllAttributes() {
        List<ProductAttributeDto> attributes = productAttribService.getAllAttributes();
        return ResponseEntity.ok(attributes);
    }

    @PostMapping
    public ResponseEntity<String> createProductAttribute(@Valid @RequestBody CreateProductAttributeDto createProductDto) {
        productAttribService.createProductAttribute(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product Attribute '" + createProductDto.name() + "' created");
    }
}
