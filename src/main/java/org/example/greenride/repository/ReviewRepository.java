package org.example.greenride.repository;

import org.example.greenride.entity.Review;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewee(User reviewee);

    List<Review> findByRide(Ride ride);
}
