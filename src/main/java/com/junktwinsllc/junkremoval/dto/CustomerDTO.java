package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.Customer;
import java.time.LocalDateTime;

public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;

    public static CustomerDTO from(Customer c) {
        CustomerDTO dto = new CustomerDTO();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.email = c.getEmail();
        dto.phone = c.getPhone();
        dto.address = c.getAddress();
        dto.createdAt = c.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}