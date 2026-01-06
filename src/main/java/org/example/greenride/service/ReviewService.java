package org.example.greenride.service;

import org.example.greenride.dto.review.ReviewRequestDTO;
import org.example.greenride.entity.Review;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.example.greenride.mapper.ReviewMapper;
import org.example.greenride.repository.ReviewRepository;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         RideRepository rideRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    public Review createReview(ReviewRequestDTO dto) {
        Ride ride = rideRepository.findById(dto.getRideId())
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        User reviewee = userRepository.findById(dto.getRevieweeId())
                .orElseThrow(() -> new IllegalArgumentException("Reviewee not found"));

        Review review = ReviewMapper.fromRequestDTO(dto, ride, reviewer, reviewee);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // update averages on reviewee
        recalcUserAverages(reviewee.getId());

        return saved;
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    public List<Review> getReviewsByRideId(Long rideId) {
        return reviewRepository.findByRideId(rideId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Reviews όπου ο χρήστης είναι reviewer ή reviewee.
     * Αν θες μόνο received reviews: use findByRevieweeId.
     */
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByReviewerIdOrRevieweeId(userId, userId);
    }

    public Review updateReview(Long id, ReviewRequestDTO dto) {
        Review existing = getReviewById(id);

        // update only rating/comment
        existing.setRating(dto.getRating());
        existing.setComment(dto.getComment());

        Review saved = reviewRepository.save(existing);

        // recalc averages for reviewee (important!)
        if (existing.getReviewee() != null) {
            recalcUserAverages(existing.getReviewee().getId());
        }

        return saved;
    }

    public void deleteReview(Long id) {
        Review existing = getReviewById(id);
        Long revieweeId = existing.getReviewee() != null ? existing.getReviewee().getId() : null;

        reviewRepository.deleteById(id);

        if (revieweeId != null) {
            recalcUserAverages(revieweeId);
        }
    }

    private void recalcUserAverages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // driver avg
        BigDecimal avgDriver = reviewRepository.avgRatingForUserAndRole(userId, "DRIVER");
        // passenger avg
        BigDecimal avgPassenger = reviewRepository.avgRatingForUserAndRole(userId, "PASSENGER");

        user.setRatingAvgDriver(scale(avgDriver));
        user.setRatingAvgPassenger(scale(avgPassenger));

        userRepository.save(user);
    }

    private BigDecimal scale(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        return v.setScale(2, RoundingMode.HALF_UP);
    }
}

