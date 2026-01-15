package org.example.greenride.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long id;

    // Relationships 

    // driver_id -> User.user_id (Many rides per one driver)
    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    // Ένα ride έχει πολλά bookings
    @OneToMany(mappedBy = "ride")
    private List<Booking> bookings;

    // Ένα ride έχει πολλά reviews
    @OneToMany(mappedBy = "ride")
    private List<Review> reviews;

    //  Fields 

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "from_city", nullable = false, length = 100)
    private String fromCity;

    @Column(name = "from_address", length = 255)
    private String fromAddress;

    @Column(name = "to_city", nullable = false, length = 100)
    private String toCity;

    @Column(name = "to_address", length = 255)
    private String toAddress;

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "estimated_duration_min")
    private Integer estimatedDurationMin;

    @Column(name = "available_seats_total", nullable = false)
    private Integer availableSeatsTotal;

    @Column(name = "available_seats_remain", nullable = false)
    private Integer availableSeatsRemain;

    @Column(name = "price_per_seat", precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerSeat;

    // PLANNED, ONGOING, COMPLETED, CANCELLED
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "weather_summary", length = 255)
    private String weatherSummary;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //  Constructors 

    public Ride() {
    }

    public Ride(User driver,
                LocalDateTime startDatetime,
                LocalDateTime endDatetime,
                String fromCity,
                String fromAddress,
                String toCity,
                String toAddress,
                BigDecimal distanceKm,
                Integer estimatedDurationMin,
                Integer availableSeatsTotal,
                Integer availableSeatsRemain,
                BigDecimal pricePerSeat,
                String status,
                String weatherSummary,
                LocalDateTime createdAt) {
        this.driver = driver;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.fromCity = fromCity;
        this.fromAddress = fromAddress;
        this.toCity = toCity;
        this.toAddress = toAddress;
        this.distanceKm = distanceKm;
        this.estimatedDurationMin = estimatedDurationMin;
        this.availableSeatsTotal = availableSeatsTotal;
        this.availableSeatsRemain = availableSeatsRemain;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
        this.weatherSummary = weatherSummary;
        this.createdAt = createdAt;
    }

    // Getters & Setters 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}



