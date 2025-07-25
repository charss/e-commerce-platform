package com.example.product_svc.dto;

import com.example.product_svc.entity.ProductVariant;

import java.util.List;

public record ProductVariantDto(
        Integer id,
        String sku,
        List<ProductVariantAttributeDto> details,
        Integer stock,
        Double price
) {
    public static ProductVariantDto from(ProductVariant variant) {
        List<ProductVariantAttributeDto> attrs = variant.getAttributes().stream()
                .map(attr -> new ProductVariantAttributeDto(
                        attr.getProductAttribute().getName(),
                        attr.getValue()
                ))
                .toList();

        return new ProductVariantDto(
                variant.getId(),
                variant.getSku(),
                attrs,
                variant.getStock(),
                variant.getPrice()
        );
    }
}
