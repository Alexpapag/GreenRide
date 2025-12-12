package org.example.greenride.repository;

import org.example.greenride.entity.Review;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRideId(Long rideId);
    List<Review> findByReviewerIdOrRevieweeId(Long reviewerId, Long revieweeId);

    @Query("select avg(r.rating) from Review r where r.reviewee.id = :userId and r.roleOfReviewee = :role")
    BigDecimal avgRatingForUserAndRole(@Param("userId") Long userId, @Param("role") String role);

}
