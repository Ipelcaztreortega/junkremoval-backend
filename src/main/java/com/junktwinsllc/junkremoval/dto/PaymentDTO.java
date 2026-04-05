package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;
    private Long jobId;
    private String contractNumber;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    public static PaymentDTO from(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.id = p.getId();
        dto.jobId = p.getJob().getId();
        dto.contractNumber = p.getJob().getContractNumber();
        dto.amount = p.getAmount();
        dto.status = p.getStatus();
        dto.paymentMethod = p.getPaymentMethod();
        dto.paidAt = p.getPaidAt();
        dto.createdAt = p.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getJobId() { return jobId; }
    public String getContractNumber() { return contractNumber; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}