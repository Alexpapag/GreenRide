package org.example.greenride.dto.ride;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RideRequestDTO {

    //@NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotNull(message = "Start datetime is required")
    @Future(message = "Start datetime must be in the future")
    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    @NotBlank(message = "From city is required")
    @Size(max = 100, message = "From city cannot exceed 100 characters")
    private String fromCity;

    @Size(max = 255, message = "From address cannot exceed 255 characters")
    private String fromAddress;

    @NotBlank(message = "To city is required")
    @Size(max = 100, message = "To city cannot exceed 100 characters")
    private String toCity;

    @Size(max = 255, message = "To address cannot exceed 255 characters")
    private String toAddress;

    @DecimalMin(value = "0.0", inclusive = false, message = "Distance must be greater than 0")
    private BigDecimal distanceKm;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDurationMin;

    @NotNull(message = "Available seats is required")
    @Min(value = 1, message = "Available seats must be at least 1")
    @Max(value = 8, message = "Available seats cannot exceed 8")
    private Integer availableSeatsTotal;

    @NotNull(message = "Price per seat is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal pricePerSeat;

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