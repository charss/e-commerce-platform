package com.example.product_svc.service;

import com.example.product_svc.dto.CreateProductAttributeDto;
import com.example.product_svc.dto.ProductAttributeDto;
import com.example.product_svc.entity.ProductAttribute;
import com.example.product_svc.repository.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeService {
    @Autowired
    ProductAttributeRepository productAttribRepo;

    public List<ProductAttributeDto> getAllAttributes() {
        return this.productAttribRepo.findAll().stream()
                .map(attribute -> new ProductAttributeDto(
                        attribute.getId(),
                        attribute.getName()
                )).toList();
    }

    public void createProductAttribute(CreateProductAttributeDto productAttributeDto) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setName(productAttributeDto.name());
        productAttribRepo.save(productAttribute);
    }
}
