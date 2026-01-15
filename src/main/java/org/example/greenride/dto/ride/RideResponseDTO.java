package org.example.greenride.dto.ride;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO για απόκριση διαδρομής (με όλα τα στοιχεία)
public class RideResponseDTO {
    private Long id; // ID διαδρομής
    private Long driverId; // ID οδηγού
    private String driverUsername; // Username οδηγού
    private String driverFullName; // Ονοματεπώνυμο οδηγού

    private LocalDateTime startDatetime; // Ώρα αναχώρησης
    private LocalDateTime endDatetime; // Ώρα άφιξης
    private String fromCity; // Πόλη αναχώρησης
    private String fromAddress; // Διεύθυνση αναχώρησης
    private String toCity; // Πόλη προορισμού
    private String toAddress; // Διεύθυνση προορισμού
    private BigDecimal distanceKm; // Απόσταση σε km
    private Integer estimatedDurationMin; // Διάρκεια σε λεπτά
    private Integer availableSeatsTotal; // Συνολικές θέσεις
    private Integer availableSeatsRemain; // Διαθέσιμες θέσεις
    private BigDecimal pricePerSeat; // Τιμή ανά θέση
    private String status; // Κατάσταση (PLANNED/COMPLETED/etc)
    private String weatherSummary; // Καιρικές συνθήκες
    private LocalDateTime createdAt; // Ημερομηνία δημιουργίας

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