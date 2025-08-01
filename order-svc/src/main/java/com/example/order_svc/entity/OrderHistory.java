package com.example.order_svc.entity;

import com.example.shared.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="order_history", schema="orders")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private OffsetDateTime changedAt;

    @Column(name = "changed_by", nullable = false, updatable = false)
    private String changedBy;

    private String remarks;

    @PrePersist
    public void prePersist() {
        if (this.changedAt == null) {
            this.changedAt = OffsetDateTime.now();
        }
    }

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(OffsetDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
