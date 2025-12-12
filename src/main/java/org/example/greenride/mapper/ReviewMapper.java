package org.example.greenride.mapper;

import org.example.greenride.dto.review.ReviewRequestDTO;
import org.example.greenride.dto.review.ReviewResponseDTO;
import org.example.greenride.entity.Review;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;

public final class ReviewMapper {
    private ReviewMapper() {}

    public static ReviewResponseDTO toResponseDTO(Review r) {
        if (r == null) return null;

        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(r.getId());

        if (r.getRide() != null) dto.setRideId(r.getRide().getId());
        if (r.getReviewer() != null) dto.setReviewerId(r.getReviewer().getId());
        if (r.getReviewee() != null) dto.setRevieweeId(r.getReviewee().getId());

        dto.setRoleOfReviewee(r.getRoleOfReviewee());
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());

        return dto;
    }

    /** create */
    public static Review fromRequestDTO(ReviewRequestDTO dto, Ride ride, User reviewer, User reviewee) {
        Review r = new Review();
        r.setRide(ride);
        r.setReviewer(reviewer);
        r.setReviewee(reviewee);
        r.setRoleOfReviewee(dto.getRoleOfReviewee());
        r.setRating(dto.getRating());
        r.setComment(dto.getComment());
        return r;
    }

    /** update (μόνο rating/comment συνήθως) */
    public static void applyUpdate(ReviewRequestDTO dto, Review r) {
        r.setRating(dto.getRating());
        r.setComment(dto.getComment());
        // roleOfReviewee/ride/reviewer/reviewee συνήθως δεν αλλάζουν
    }
}
