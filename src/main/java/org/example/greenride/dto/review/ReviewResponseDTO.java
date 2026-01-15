package org.example.greenride.dto.review;

import java.time.LocalDateTime;
// DTO για απόκριση αξιολόγησης

public class ReviewResponseDTO {
    private Long id;

    private Long rideId;
    private Long reviewerId;
    private Long revieweeId;

    private String roleOfReviewee;
    private Integer rating;
    private String comment;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Long getRevieweeId() { return revieweeId; }
    public void setRevieweeId(Long revieweeId) { this.revieweeId = revieweeId; }

    public String getRoleOfReviewee() { return roleOfReviewee; }
    public void setRoleOfReviewee(String roleOfReviewee) { this.roleOfReviewee = roleOfReviewee; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
