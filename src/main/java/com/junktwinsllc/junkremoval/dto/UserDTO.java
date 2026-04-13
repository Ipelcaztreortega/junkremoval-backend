package com.junktwinsllc.junkremoval.dto;

import com.junktwinsllc.junkremoval.model.User;
import java.time.LocalDateTime;

public class UserDTO {

    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.role = user.getRole();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}