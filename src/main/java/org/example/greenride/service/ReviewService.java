package org.example.greenride.service;

import org.example.greenride.entity.Review;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.example.greenride.repository.ReviewRepository;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // CREATE
    public Review createReview(Long rideId,
                               Long reviewerId,
                               Long revieweeId,
                               String roleOfReviewee,
                               Integer rating,
                               String comment) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        User reviewee = userRepository.findById(revieweeId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewee not found"));

        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (!"COMPLETED".equalsIgnoreCase(ride.getStatus())) {
            throw new IllegalStateException("Can only review completed rides");
        }
        if (reviewer.getId().equals(reviewee.getId())) {
            throw new IllegalStateException("User cannot review themselves");
        }

        Review review = new Review();
        review.setRide(ride);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRoleOfReviewee(roleOfReviewee);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        recalculateAndUpdateUserRatings(reviewee);
        return saved;

    }
    private void recalculateAndUpdateUserRatings(User reviewee) {
        // Fetch all reviews where this user is the reviewee
        List<Review> allReviewsForUser = reviewRepository.findByReviewee(reviewee);

        BigDecimal driverAvg = calculateAverageRatingForRole(allReviewsForUser, "DRIVER");
        BigDecimal passengerAvg = calculateAverageRatingForRole(allReviewsForUser, "PASSENGER");

        reviewee.setRatingAvgDriver(driverAvg);
        reviewee.setRatingAvgPassenger(passengerAvg);

        userRepository.save(reviewee);
    }
    private BigDecimal calculateAverageRatingForRole(List<Review> reviews, String role) {
        int sum = 0;
        int count = 0;

        for (Review r : reviews) {
            if (r.getRoleOfReviewee() != null
                    && r.getRoleOfReviewee().equalsIgnoreCase(role)
                    && r.getRating() != null) {

                sum += r.getRating();
                count++;
            }
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
    // READ ALL
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // READ ONE
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    // READ: reviews για συγκεκριμένο χρήστη (ως reviewee)
    public List<Review> getReviewsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return reviewRepository.findByReviewee(user);
    }

    // READ: reviews για συγκεκριμένο ride
    public List<Review> getReviewsForRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));
        return reviewRepository.findByRide(ride);
    }

    // UPDATE μόνο rating/comment
    public Review updateReview(Long id, Integer rating, String comment) {
        Review existing = getReviewById(id);

        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            existing.setRating(rating);
        }

        if (comment != null) {
            existing.setComment(comment);
        }

        return reviewRepository.save(existing);
    }

    // DELETE
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.deleteById(id);
    }
}
