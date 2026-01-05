package org.example.greenride.dto.ride;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RideResponseDTO {
    private Long id;
    private Long driverId;
    private String driverUsername;
    private String driverFullName; // ΠΡΟΣΘΗΚΗ ΑΥΤΗ

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String fromCity;
    private String fromAddress;
    private String toCity;
    private String toAddress;
    private BigDecimal distanceKm;
    private Integer estimatedDurationMin;
    private Integer availableSeatsTotal;
    private Integer availableSeatsRemain;
    private BigDecimal pricePerSeat;
    private String status;
    private String weatherSummary;
    private LocalDateTime createdAt;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverUsername() { return driverUsername; }
    public void setDriverUsername(String driverUsername) { this.driverUsername = driverUsername; }

    public String getDriverFullName() { return driverFullName; }
    public void setDriverFullName(String driverFullName) { this.driverFullName = driverFullName; }

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

    public Integer getAvailableSeatsRemain() { return availableSeatsRemain; }
    public void setAvailableSeatsRemain(Integer availableSeatsRemain) { this.availableSeatsRemain = availableSeatsRemain; }

    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(BigDecimal pricePerSeat) { this.pricePerSeat = pricePerSeat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWeatherSummary() { return weatherSummary; }
    public void setWeatherSummary(String weatherSummary) { this.weatherSummary = weatherSummary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}