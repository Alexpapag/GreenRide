package org.example.greenride.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    // ACTIVE, BANNED, SUSPENDED
    @Column(nullable = false, length = 20)
    private String status;

    // USER, ADMIN
    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "rating_avg_driver", precision = 3, scale = 2)
    private BigDecimal ratingAvgDriver;

    @Column(name = "rating_avg_passenger", precision = 3, scale = 2)
    private BigDecimal ratingAvgPassenger;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Ένας χρήστης ως driver έχει πολλά rides
    @OneToMany(mappedBy = "driver")
    private List<Ride> ridesAsDriver;

    // Ένας χρήστης ως passenger έχει πολλά bookings
    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookingsAsPassenger;

    // Reviews που έγραψε ο user
    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviewsWritten;

    // Reviews που έλαβε ο user (ως driver ή passenger)
    @OneToMany(mappedBy = "reviewee")
    private List<Review> reviewsReceived;

    // Reports όπου είναι ο αναφερόμενος
    @OneToMany(mappedBy = "reportedUser")
    private List<UserReport> reportsReceived;

    // Reports που έχει κάνει ο ίδιος
    @OneToMany(mappedBy = "reporterUser")
    private List<UserReport> reportsMade;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Constructors
    public User() {
    }

    public User(String username,
                String password,
                String fullName,
                String email,
                String phone,
                String status,
                String role,
                BigDecimal ratingAvgDriver,
                BigDecimal ratingAvgPassenger,
                LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.role = role;
        this.ratingAvgDriver = ratingAvgDriver;
        this.ratingAvgPassenger = ratingAvgPassenger;
        this.createdAt = createdAt;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getRatingAvgDriver() {
        return ratingAvgDriver;
    }

    public void setRatingAvgDriver(BigDecimal ratingAvgDriver) {
        this.ratingAvgDriver = ratingAvgDriver;
    }

    public BigDecimal getRatingAvgPassenger() {
        return ratingAvgPassenger;
    }

    public void setRatingAvgPassenger(BigDecimal ratingAvgPassenger) {
        this.ratingAvgPassenger = ratingAvgPassenger;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Ride> getRidesAsDriver() {
        return ridesAsDriver;
    }

    public void setRidesAsDriver(List<Ride> ridesAsDriver) {
        this.ridesAsDriver = ridesAsDriver;
    }

    public List<Booking> getBookingsAsPassenger() {
        return bookingsAsPassenger;
    }

    public void setBookingsAsPassenger(List<Booking> bookingsAsPassenger) {
        this.bookingsAsPassenger = bookingsAsPassenger;
    }

    public List<Review> getReviewsWritten() {
        return reviewsWritten;
    }

    public void setReviewsWritten(List<Review> reviewsWritten) {
        this.reviewsWritten = reviewsWritten;
    }

    public List<Review> getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(List<Review> reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }

    public List<UserReport> getReportsReceived() {
        return reportsReceived;
    }

    public void setReportsReceived(List<UserReport> reportsReceived) {
        this.reportsReceived = reportsReceived;
    }

    public List<UserReport> getReportsMade() {
        return reportsMade;
    }

    public void setReportsMade(List<UserReport> reportsMade) {
        this.reportsMade = reportsMade;
    }
}
