package com.example.product_svc.service;

import com.example.product_svc.dto.CreateProductVariantDto;
import com.example.product_svc.dto.VariantAttributeDto;
import com.example.product_svc.entity.Product;
import com.example.product_svc.entity.ProductAttribute;
import com.example.product_svc.entity.ProductVariant;
import com.example.product_svc.entity.ProductVariantAttribute;
import com.example.product_svc.exception.InvalidMovementTypeException;
import com.example.product_svc.exception.InvalidQuantityException;
import com.example.product_svc.exception.SkuAlreadyExistsException;
import com.example.product_svc.mapper.ProductVariantMapper;
import com.example.product_svc.repository.ProductAttributeRepository;
import com.example.product_svc.repository.ProductRepository;
import com.example.product_svc.repository.ProductVariantAttributeRepository;
import com.example.product_svc.repository.ProductVariantRepository;
import com.example.shared.dto.ProductVariantBasicDto;
import com.example.shared.enums.MovementType;
import com.example.shared.exception.ObjectNotFoundException;
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

    public ProductVariantBasicDto getProductVariantById(Integer id) {
        ProductVariant productVariant = productVariantRepo.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Product Variant", id));
        return ProductVariantMapper.toDto(productVariant);
    }

    @Transactional
    public ProductVariantBasicDto createProductVariant(CreateProductVariantDto productVariantDto) {
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
        return ProductVariantMapper.toDto(savedVariant);
    }

    private ProductVariant buildVariant(CreateProductVariantDto dto, Product product) {
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(dto.sku());
        variant.setUnitPriceMinor(dto.unitPrice());
        variant.setCurrency(dto.currency());
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
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative");
        }

        if (movementType.isDeduct()) {
            if (quantity > variant.getStock()) {
                throw new InvalidQuantityException("Invalid item " + variant.getSku() +
                        ". Requested stock (" + quantity + ") exceeds available stock (" + variant.getStock() + ")");
            }
            variant.setStock(variant.getStock() - quantity);
        } else if (movementType.isAdd())  {
            variant.setStock(variant.getStock() + quantity);
        } else {
            throw new InvalidMovementTypeException("Unsupported movement type: " + movementType);
        }
        productVariantRepo.save(variant);
    }

    public void reverseStock(ProductVariant variant, MovementType movementType, Integer quantity) {
        if (movementType == MovementType.IN) {
            variant.setStock(variant.getStock() - quantity);
        } else {
            variant.setStock(variant.getStock() + quantity);
        }
    }
}
