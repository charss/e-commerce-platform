package com.example.product_svc.mapper;

import com.example.product_svc.entity.ProductVariant;
import com.example.shared.dto.ProductVariantAttributeDto;
import com.example.shared.dto.ProductVariantBasicDto;
import com.example.shared.dto.ProductVariantResponseDto;
import com.example.shared.util.MoneyUtil;

import java.util.List;

public class ProductVariantMapper {
    public static ProductVariantBasicDto toDto(ProductVariant variant) {
        List<ProductVariantAttributeDto> attrs = variant.getAttributes().stream()
                .map(attr -> new ProductVariantAttributeDto(
                        attr.getProductAttribute().getName(),
                        attr.getValue()
                ))
                .toList();

        return new ProductVariantBasicDto(
                variant.getId(),
                variant.getProduct().getUid(),
                variant.getSku(),
                attrs,
                variant.getStock(),
                variant.getUnitPriceMinor(),
                variant.getCurrency()
        );
    }

    public static ProductVariantResponseDto toResponseDto(ProductVariant variant) {
        List<ProductVariantAttributeDto> attrs = variant.getAttributes().stream()
                .map(attr -> new ProductVariantAttributeDto(
                        attr.getProductAttribute().getName(),
                        attr.getValue()
                ))
                .toList();

        return new ProductVariantResponseDto(
                variant.getId(),
                variant.getProduct().getUid(),
                variant.getSku(),
                attrs,
                variant.getStock(),
                MoneyUtil.format(variant.getUnitPriceMinor(), variant.getCurrency())
        );
    }
}
