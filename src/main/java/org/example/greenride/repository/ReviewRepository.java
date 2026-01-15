package org.example.greenride.repository;

import org.example.greenride.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

// Repository για διαχείριση Review entities (αξιολογήσεις)
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Αναζήτηση αξιολογήσεων για συγκεκριμένη διαδρομή
    List<Review> findByRideId(Long rideId);

    // Αναζήτηση αξιολογήσεων όπου ο χρήστης είναι reviewer ή reviewee
    List<Review> findByReviewerIdOrRevieweeId(Long reviewerId, Long revieweeId);

    // Υπολογισμός μέσου όρου rating για χρήστη με συγκεκριμένο ρόλο (DRIVER/PASSENGER)
    @Query("""
           select avg(r.rating)
           from Review r
           where r.reviewee.id = :userId
             and r.roleOfReviewee = :role
           """)
    BigDecimal avgRatingForUserAndRole(@Param("userId") Long userId,
                                       @Param("role") String role);
}

