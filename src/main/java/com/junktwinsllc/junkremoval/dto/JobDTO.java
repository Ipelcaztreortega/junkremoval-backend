package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class JobDTO {

    private Long id;
    private String contractNumber;
    private String status;
    private String volume;
    private String difficulty;
    private BigDecimal distanceMiles;
    private BigDecimal agreedPrice;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;
    private CustomerDTO customer;
    private List<JobItemDTO> items;

    public static JobDTO from(Job job) {
        JobDTO dto = new JobDTO();
        dto.id = job.getId();
        dto.contractNumber = job.getContractNumber();
        dto.status = job.getStatus();
        dto.volume = job.getVolume();
        dto.difficulty = job.getDifficulty();
        dto.distanceMiles = job.getDistanceMiles();
        dto.agreedPrice = job.getAgreedPrice();
        dto.scheduledAt = job.getScheduledAt();
        dto.createdAt = job.getCreatedAt();

        if (job.getCustomer() != null) {
            dto.customer = CustomerDTO.from(job.getCustomer());
        }
        if (job.getItems() != null) {
            dto.items = job.getItems().stream()
                    .map(JobItemDTO::from)
                    .collect(Collectors.toList());
        }
        return dto;
    }

    public Long getId() { return id; }
    public String getContractNumber() { return contractNumber; }
    public String getStatus() { return status; }
    public String getVolume() { return volume; }
    public String getDifficulty() { return difficulty; }
    public BigDecimal getDistanceMiles() { return distanceMiles; }
    public BigDecimal getAgreedPrice() { return agreedPrice; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CustomerDTO getCustomer() { return customer; }
    public List<JobItemDTO> getItems() { return items; }
}