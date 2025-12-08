package org.example.greenride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    // ride_id -> Ride.ride_id
    @ManyToOne(optional = false)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    // reviewer_id -> User.user_id (ο χρήστης που γράφει το review)
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    // reviewee_id -> User.user_id (ο χρήστης που αξιολογείται)
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

    // DRIVER or PASSENGER
    @Column(name = "role_of_reviewee", nullable = false, length = 20)
    private String roleOfReviewee;

    // 1..5
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // ---------- Constructors ----------

    public Review() {
    }

    public Review(Ride ride,
                  User reviewer,
                  User reviewee,
                  String roleOfReviewee,
                  Integer rating,
                  String comment,
                  LocalDateTime createdAt) {
        this.ride = ride;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.roleOfReviewee = roleOfReviewee;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public void setReviewee(User reviewee) {
        this.reviewee = reviewee;
    }

    public String getRoleOfReviewee() {
        return roleOfReviewee;
    }

    public void setRoleOfReviewee(String roleOfReviewee) {
        this.roleOfReviewee = roleOfReviewee;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
