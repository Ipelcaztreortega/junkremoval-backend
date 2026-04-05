package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.Quote;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuoteDTO {

    private Long id;
    private Long jobId;
    private String contractNumber;
    private BigDecimal volumePrice;
    private BigDecimal itemPrice;
    private BigDecimal weightPrice;
    private BigDecimal distanceFee;
    private BigDecimal difficultyFee;
    private BigDecimal dumpFee;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    public static QuoteDTO from(Quote q) {
        QuoteDTO dto = new QuoteDTO();
        dto.id = q.getId();
        dto.jobId = q.getJob().getId();
        dto.contractNumber = q.getJob().getContractNumber();
        dto.volumePrice = q.getVolumePrice();
        dto.itemPrice = q.getItemPrice();
        dto.weightPrice = q.getWeightPrice();
        dto.distanceFee = q.getDistanceFee();
        dto.difficultyFee = q.getDifficultyFee();
        dto.dumpFee = q.getDumpFee();
        dto.totalPrice = q.getTotalPrice();
        dto.createdAt = q.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getJobId() { return jobId; }
    public String getContractNumber() { return contractNumber; }
    public BigDecimal getVolumePrice() { return volumePrice; }
    public BigDecimal getItemPrice() { return itemPrice; }
    public BigDecimal getWeightPrice() { return weightPrice; }
    public BigDecimal getDistanceFee() { return distanceFee; }
    public BigDecimal getDifficultyFee() { return difficultyFee; }
    public BigDecimal getDumpFee() { return dumpFee; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}