package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.JobItem;
import java.math.BigDecimal;

public class JobItemDTO {

    private Long id;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static JobItemDTO from(JobItem item) {
        JobItemDTO dto = new JobItemDTO();
        dto.id = item.getId();
        dto.itemName = item.getItemName();
        dto.quantity = item.getQuantity();
        dto.unitPrice = item.getUnitPrice();
        dto.totalPrice = item.getTotalPrice();
        return dto;
    }

    public Long getId() { return id; }
    public String getItemName() { return itemName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}