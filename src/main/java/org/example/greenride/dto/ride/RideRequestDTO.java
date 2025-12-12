package org.example.greenride.dto.ride;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RideRequestDTO {

    @NotNull
    private Long driverId; // για να αντικαταστήσει το @RequestParam driverId

    @NotNull
    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    @NotBlank
    @Size(max = 100)
    private String fromCity;

    @Size(max = 255)
    private String fromAddress;

    @NotBlank
    @Size(max = 100)
    private String toCity;

    @Size(max = 255)
    private String toAddress;

    @PositiveOrZero
    private BigDecimal distanceKm;

    @PositiveOrZero
    private Integer estimatedDurationMin;

    @NotNull
    @Min(1)
    private Integer availableSeatsTotal;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal pricePerSeat;

    // getters/setters
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
