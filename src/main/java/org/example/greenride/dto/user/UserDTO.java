package org.example.greenride.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// DTO για πληροφορίες χρήστη

public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private String role;

    private BigDecimal ratingAvgDriver;
    private BigDecimal ratingAvgPassenger;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public BigDecimal getRatingAvgDriver() { return ratingAvgDriver; }
    public void setRatingAvgDriver(BigDecimal ratingAvgDriver) { this.ratingAvgDriver = ratingAvgDriver; }

    public BigDecimal getRatingAvgPassenger() { return ratingAvgPassenger; }
    public void setRatingAvgPassenger(BigDecimal ratingAvgPassenger) { this.ratingAvgPassenger = ratingAvgPassenger; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
