package com.example.order_svc.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="order_item", schema="orders")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_variant_id", nullable = false)
    private Integer productVariantId;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long unitPriceMinor;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private Long subtotalMinor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Integer productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Long getSubtotalMinor() {
        return subtotalMinor;
    }

    public void setSubtotalMinor(Long subtotalMinor) {
        this.subtotalMinor = subtotalMinor;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", productVariantId=" + productVariantId +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", unitPriceMinor=" + unitPriceMinor +
                ", currency='" + currency + '\'' +
                ", subTotalMinor=" + subtotalMinor +
                '}';
    }
}
