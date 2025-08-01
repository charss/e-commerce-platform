package com.example.product_svc.service;

import com.example.product_svc.dto.CreateProductDto;
import com.example.product_svc.entity.Product;
import com.example.product_svc.mapper.ProductVariantMapper;
import com.example.product_svc.repository.ProductRepository;
import com.example.shared.dto.ProductDto;
import com.example.shared.dto.ProductVariantBasicDto;
import com.example.shared.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepo;

    public List<ProductDto> getAllProducts() {
        return this.productRepo.findAll().stream()
                .map(product -> new ProductDto(
                        product.getUid(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategoryList(),
                        product.getVariants().stream().map(
                                ProductVariantMapper::toDto
                        ).toList()
                )).toList();
    }

    public List<ProductVariantBasicDto> getAllVariantsByProduct(UUID uid) {
        Product product = productRepo.findById(uid).orElseThrow(
                () -> new ObjectNotFoundException("Product", uid));

        return product.getVariants().stream()
                .map(ProductVariantMapper::toDto).toList();
    }

    public Product createProduct(CreateProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        return productRepo.save(product);
    }
}
