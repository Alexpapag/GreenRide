package org.example.greenride.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequestDTO {

    @NotNull
    private Long rideId;

    @NotNull
    private Long reviewerId;

    @NotNull
    private Long revieweeId;

    @NotBlank
    @Size(max = 20)
    private String roleOfReviewee; // "DRIVER" or "PASSENGER" (όπως το έχεις)

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 1000)
    private String comment;

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
}
