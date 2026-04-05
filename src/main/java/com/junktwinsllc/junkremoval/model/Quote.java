package com.junktwinsllc.junkremoval.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One quote per job
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Job job;

    // Volume-based price: QUARTER=75, HALF=150, FULL=275, NONE=0
    @Column(name = "volume_price", precision = 10, scale = 2)
    private BigDecimal volumePrice = BigDecimal.ZERO;

    // Sum of all JobItem totalPrices
    @Column(name = "item_price", precision = 10, scale = 2)
    private BigDecimal itemPrice = BigDecimal.ZERO;

    // Weight surcharge if haul is unusually heavy
    @Column(name = "weight_price", precision = 10, scale = 2)
    private BigDecimal weightPrice = BigDecimal.ZERO;

    // Gas/travel: distanceMiles * rate per mile
    @Column(name = "distance_fee", precision = 10, scale = 2)
    private BigDecimal distanceFee = BigDecimal.ZERO;

    // Difficulty surcharge: STAIRS=25, LADDER=50, DEMO=100, STANDARD=0
    @Column(name = "difficulty_fee", precision = 10, scale = 2)
    private BigDecimal difficultyFee = BigDecimal.ZERO;

    // Dump/disposal fee
    @Column(name = "dump_fee", precision = 10, scale = 2)
    private BigDecimal dumpFee = BigDecimal.ZERO;

    // Sum of all fields above — calculated before saving
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    protected void calculateTotal() {
        this.totalPrice = volumePrice
                .add(itemPrice)
                .add(weightPrice)
                .add(distanceFee)
                .add(difficultyFee)
                .add(dumpFee);
        if (createdAt == null) this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Job getJob() { return job; }
    public BigDecimal getVolumePrice() { return volumePrice; }
    public BigDecimal getItemPrice() { return itemPrice; }
    public BigDecimal getWeightPrice() { return weightPrice; }
    public BigDecimal getDistanceFee() { return distanceFee; }
    public BigDecimal getDifficultyFee() { return difficultyFee; }
    public BigDecimal getDumpFee() { return dumpFee; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setJob(Job job) { this.job = job; }
    public void setVolumePrice(BigDecimal volumePrice) { this.volumePrice = volumePrice; }
    public void setItemPrice(BigDecimal itemPrice) { this.itemPrice = itemPrice; }
    public void setWeightPrice(BigDecimal weightPrice) { this.weightPrice = weightPrice; }
    public void setDistanceFee(BigDecimal distanceFee) { this.distanceFee = distanceFee; }
    public void setDifficultyFee(BigDecimal difficultyFee) { this.difficultyFee = difficultyFee; }
    public void setDumpFee(BigDecimal dumpFee) { this.dumpFee = dumpFee; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}