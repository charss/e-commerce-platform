package com.example.product_svc.controller;

import com.example.product_svc.dto.CreateProductDto;
import com.example.product_svc.entity.Product;
import com.example.product_svc.mapper.ProductVariantMapper;
import com.example.product_svc.repository.ProductAttributeRepository;
import com.example.product_svc.service.ProductService;
import com.example.shared.dto.ProductDto;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductAttributeRepository productAttribRepo;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/variant")
    public ResponseEntity<List<ProductVariantBasicDto>> getAllProductVariants(@PathVariable(value = "id") UUID id) {
        List<ProductVariantBasicDto> products = productService.getAllVariantsByProduct(id);
        return ResponseEntity.ok(products);
    }


    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        Product product = productService.createProduct(createProductDto);
        ProductDto productDto = new ProductDto(
                product.getUid(),
                product.getName(),
                product.getDescription(),
                product.getVariants().stream().map(
                        ProductVariantMapper::toDto
                ).toList()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }
}
