package com.example.product_svc.service;

import com.example.product_svc.common.MovementType;
import com.example.product_svc.dto.CreateProductVariantDto;
import com.example.product_svc.dto.ProductVariantDto;
import com.example.product_svc.dto.VariantAttributeDto;
import com.example.product_svc.entity.Product;
import com.example.product_svc.entity.ProductAttribute;
import com.example.product_svc.entity.ProductVariant;
import com.example.product_svc.entity.ProductVariantAttribute;
import com.example.product_svc.exception.ObjectNotFoundException;
import com.example.product_svc.exception.SkuAlreadyExistsException;
import com.example.product_svc.repository.ProductAttributeRepository;
import com.example.product_svc.repository.ProductRepository;
import com.example.product_svc.repository.ProductVariantAttributeRepository;
import com.example.product_svc.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    ProductVariantRepository productVariantRepo;
    @Autowired
    ProductRepository productRepo;
    @Autowired
    ProductVariantAttributeRepository productVariantAttributeRepository;
    @Autowired
    ProductAttributeRepository productAttribRepo;

    public ProductVariantDto getProductVariantById(Integer id) {
        ProductVariant productVariant = productVariantRepo.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", id));
        return ProductVariantDto.from(productVariant);
    }

    @Transactional
    public ProductVariantDto createProductVariant(CreateProductVariantDto productVariantDto) {
        if (productVariantRepo.existsBySku(productVariantDto.sku())) {
            throw new SkuAlreadyExistsException("SKU '" + productVariantDto.sku() + "' already exists.");
        }

        ProductVariant savedVariant = productRepo.findById(productVariantDto.productId())
                .map(product -> {
                    ProductVariant variant = buildVariant(productVariantDto, product);
                    return productVariantRepo.save(variant);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Product", productVariantDto.productId()));

        List<ProductVariantAttribute> attributes = productVariantDto.attributes().stream()
                .map(attr -> buildVariantAttribute(attr, savedVariant))
                .toList();
        productVariantAttributeRepository.saveAll(attributes);

        savedVariant.setAttributes(attributes);
        return ProductVariantDto.from(savedVariant);
    }

    private ProductVariant buildVariant(CreateProductVariantDto dto, Product product) {
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(dto.sku());
        variant.setPrice(0.0);
        variant.setStock(0);
        return variant;
    }

    private ProductVariantAttribute buildVariantAttribute(VariantAttributeDto dto, ProductVariant variant) {
        ProductAttribute attr = productAttribRepo.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        ProductVariantAttribute variantAttrib = new ProductVariantAttribute();
        variantAttrib.setProductVariant(variant);
        variantAttrib.setProductAttribute(attr);
        variantAttrib.setValue(dto.value());
        return variantAttrib;
    }

    public void updateStock(ProductVariant variant, MovementType movementType, Integer quantity) {
        if (movementType == MovementType.OUT) {
            variant.setStock(variant.getStock() - quantity);
        } else {
            variant.setStock(variant.getStock() + quantity);
        }
    }

    public void reverseStock(ProductVariant variant, MovementType movementType, Integer quantity) {
        if (movementType == MovementType.IN) {
            variant.setStock(variant.getStock() - quantity);
        } else {
            variant.setStock(variant.getStock() + quantity);
        }
    }
}
