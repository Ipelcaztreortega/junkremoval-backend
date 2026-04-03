package com.junktwinsllc.junkremoval.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "job_item")
public class JobItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    // Price per single item
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    // quantity * unitPrice — calculated before saving
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @PrePersist
    @PreUpdate
    protected void calculateTotal() {
        if (quantity != null && unitPrice != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public Long getId() { return id; }
    public Job getJob() { return job; }
    public String getItemName() { return itemName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setId(Long id) { this.id = id; }
    public void setJob(Job job) { this.job = job; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}