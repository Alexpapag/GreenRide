package org.example.greenride.dto.ride;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO για αίτημα δημιουργίας/ενημέρωσης διαδρομής
public class RideRequestDTO {

    private Long driverId; // ID οδηγού

    @NotNull(message = "Start datetime is required")
    @Future(message = "Start datetime must be in the future")
    private LocalDateTime startDatetime; // Ώρα αναχώρησης

    private LocalDateTime endDatetime; // Ώρα άφιξης

    @NotBlank(message = "From city is required")
    @Size(max = 100, message = "From city cannot exceed 100 characters")
    private String fromCity; // Πόλη αναχώρησης

    @Size(max = 255, message = "From address cannot exceed 255 characters")
    private String fromAddress; // Διεύθυνση αναχώρησης

    @NotBlank(message = "To city is required")
    @Size(max = 100, message = "To city cannot exceed 100 characters")
    private String toCity; // Πόλη προορισμού

    @Size(max = 255, message = "To address cannot exceed 255 characters")
    private String toAddress; // Διεύθυνση προορισμού

    @DecimalMin(value = "0.0", inclusive = false, message = "Distance must be greater than 0")
    private BigDecimal distanceKm; // Απόσταση σε km

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDurationMin; // Διάρκεια σε λεπτά

    @NotNull(message = "Available seats is required")
    @Min(value = 1, message = "Available seats must be at least 1")
    @Max(value = 8, message = "Available seats cannot exceed 8")
    private Integer availableSeatsTotal; // Διαθέσιμες θέσεις

    @NotNull(message = "Price per seat is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal pricePerSeat; // Τιμή ανά θέση

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public LocalDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(LocalDateTime startDatetime) { this.startDatetime = startDatetime; }

    public LocalDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(LocalDateTime endDatetime) { this.endDatetime = endDatetime; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public Integer getEstimatedDurationMin() { return estimatedDurationMin; }
    public void setEstimatedDurationMin(Integer estimatedDurationMin) { this.estimatedDurationMin = estimatedDurationMin; }

    public Integer getAvailableSeatsTotal() { return availableSeatsTotal; }
    public void setAvailableSeatsTotal(Integer availableSeatsTotal) { this.availableSeatsTotal = availableSeatsTotal; }

    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(BigDecimal pricePerSeat) { this.pricePerSeat = pricePerSeat; }
}