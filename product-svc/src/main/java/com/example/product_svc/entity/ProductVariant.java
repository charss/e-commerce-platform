package com.example.product_svc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="product_variant", schema="product")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    @ToString.Exclude
    private List<ProductVariantAttribute> attributes;

    @OneToMany(mappedBy = "productVariant")
    @ToString.Exclude
    private List<InventoryMovement> inventoryMovements;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer stock;

    @Column(nullable = false)
    private Long unitPriceMinor;

    @Column(nullable = false, length = 3)
    private String currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<ProductVariantAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductVariantAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<InventoryMovement> getInventoryMovements() {
        return inventoryMovements;
    }

    public void setInventoryMovements(List<InventoryMovement> inventoryMovements) {
        this.inventoryMovements = inventoryMovements;
    }

    public Long getUnitPriceMinor() {
        return unitPriceMinor;
    }

    public void setUnitPriceMinor(Long unitPriceMinor) {
        this.unitPriceMinor = unitPriceMinor;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", product=" + product +
                ", attributes=" + attributes +
                ", movements=" + inventoryMovements +
                ", sku='" + sku + '\'' +
                ", stock=" + stock +
                ", unitPriceMinor=" + unitPriceMinor +
                '}';
    }
}
