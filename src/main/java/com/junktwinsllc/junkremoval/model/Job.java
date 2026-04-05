package com.junktwinsllc.junkremoval.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "contract_number", unique = true)
    private String contractNumber;

    // Lifecycle: QUOTED → CONFIRMED → COMPLETED → PAID → CANCELLED
    @Column(nullable = false)
    private String status = "QUOTED";

    // QUARTER, HALF, FULL, or NONE for item-only jobs
    private String volume;

    // STANDARD, STAIRS, LADDER, DEMO
    private String difficulty;

    @Column(name = "distance_miles", precision = 6, scale = 2)
    private BigDecimal distanceMiles;

    @Column(name = "agreed_price", precision = 10, scale = 2)
    private BigDecimal agreedPrice;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobItem> items;

//    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Quote quote;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.contractNumber == null) {
            this.contractNumber = "JT-" + String.valueOf(System.currentTimeMillis()).substring(8);
        }
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public String getContractNumber() { return contractNumber; }
    public String getStatus() { return status; }
    public String getVolume() { return volume; }
    public String getDifficulty() { return difficulty; }
    public BigDecimal getDistanceMiles() { return distanceMiles; }
    public BigDecimal getAgreedPrice() { return agreedPrice; }
    public List<JobItem> getItems() { return items; }
//    public Quote getQuote() { return quote; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setVolume(String volume) { this.volume = volume; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setDistanceMiles(BigDecimal distanceMiles) { this.distanceMiles = distanceMiles; }
    public void setAgreedPrice(BigDecimal agreedPrice) { this.agreedPrice = agreedPrice; }
    public void setItems(List<JobItem> items) { this.items = items; }
//    public void setQuote(Quote quote) { this.quote = quote; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}