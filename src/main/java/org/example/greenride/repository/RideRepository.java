package org.example.greenride.repository;

import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// Repository για διαχείριση Ride entities (διαδρομές)
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Αναζήτηση διαδρομών από πόλη σε πόλη
    List<Ride> findByFromCityAndToCity(String fromCity, String toCity);

    // Αναζήτηση διαδρομών από πόλη σε πόλη με συγκεκριμένο status
    List<Ride> findByFromCityAndToCityAndStatus(String fromCity, String toCity, String status);

    // Αναζήτηση διαδρομών με βάση χρονικό διάστημα εκκίνησης
    List<Ride> findByStartDatetimeBetween(LocalDateTime startFrom, LocalDateTime startTo);

    // Αναζήτηση διαδρομών συγκεκριμένου οδηγού
    List<Ride> findByDriver(User driver);
    // Αναζήτηση διαδρομών (εκτός από συγκεκριμένο οδηγό) με status
    List<Ride> findByDriverIdNotAndStatus(Long driverId, String status);

    // Αναζήτηση διαθέσιμων διαδρομών (με status και ελάχιστες θέσεις)
    List<Ride> findByStatusAndAvailableSeatsRemainGreaterThan(String status, Integer minSeats);

    // Μέτρηση διαδρομών με βάση status
    long countByStatus(String status);

    // Μέτρηση διαδρομών που δημιουργήθηκαν μετά από συγκεκριμένη ημερομηνία
    @Query("SELECT COUNT(r) FROM Ride r WHERE r.createdAt >= :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    // Αναζήτηση 5 πιο πρόσφατων διαδρομών
    List<Ride> findTop5ByOrderByCreatedAtDesc();

    // Αναζήτηση διαδρομών με βάση status
    List<Ride> findByStatus(String status);
}