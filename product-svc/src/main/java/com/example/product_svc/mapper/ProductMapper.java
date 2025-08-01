package com.example.product_svc.mapper;

import com.example.product_svc.entity.Product;
import com.example.shared.dto.ProductDto;
import com.example.shared.dto.ProductVariantBasicDto;

import java.util.List;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        List<ProductVariantBasicDto> variants = product.getVariants().stream()
                .map(ProductVariantMapper::toDto)
                .toList();

        return new ProductDto(
                product.getUid(),
                product.getName(),
                product.getDescription(),
                variants
        );
    }
}
