package org.example.greenride.repository;

import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    // Αναζήτηση από πόλη σε πόλη
    List<Ride> findByFromCityAndToCity(String fromCity, String toCity);

    // Αναζήτηση από πόλη σε πόλη + status (π.χ. PLANNED)
    List<Ride> findByFromCityAndToCityAndStatus(String fromCity, String toCity, String status);

    // Αναζήτηση με βάση ημερομηνία/ώρα εκκίνησης
    List<Ride> findByStartDatetimeBetween(LocalDateTime startFrom, LocalDateTime startTo);

    List<Ride> findByDriver(User driver);
    List<Ride> findByDriverIdNotAndStatus(Long driverId, String status);

    // Available rides (status + seats)
    List<Ride> findByStatusAndAvailableSeatsRemainGreaterThan(String status, Integer minSeats);

    // New methods for AdminService
    long countByStatus(String status);

    @Query("SELECT COUNT(r) FROM Ride r WHERE r.createdAt >= :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    List<Ride> findTop5ByOrderByCreatedAtDesc();

    List<Ride> findByStatus(String status);
}