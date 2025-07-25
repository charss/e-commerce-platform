package com.example.product_svc.service;

import com.example.product_svc.dto.CreateProductDto;
import com.example.product_svc.dto.ProductDto;
import com.example.product_svc.dto.ProductVariantDto;
import com.example.product_svc.entity.Product;
import com.example.product_svc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                                ProductVariantDto::from
                        ).toList()
                )).toList();
    }

    public Product createProduct(CreateProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        return productRepo.save(product);
    }
}
