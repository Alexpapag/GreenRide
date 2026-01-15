package org.example.greenride.repository;

import org.example.greenride.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Repository για διαχείριση Booking entities (κρατήσεις)
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Αναζήτηση κρατήσεων για συγκεκριμένη διαδρομή
    List<Booking> findByRideId(Long rideId);

    // Αναζήτηση κρατήσεων συγκεκριμένου επιβάτη
    List<Booking> findByPassengerId(Long passengerId);

    // Αναζήτηση κρατήσεων συγκεκριμένου επιβάτη με status
    List<Booking> findByPassengerIdAndStatus(Long passengerId, String status);

    // Έλεγχος αν υπάρχει κράτηση για συγκεκριμένη διαδρομή και επιβάτη
    boolean existsByRideIdAndPassengerId(Long rideId, Long passengerId);

    // Μέτρηση κρατήσεων με βάση status
    long countByStatus(String status);

    // Μέτρηση κρατήσεων που δημιουργήθηκαν μετά από συγκεκριμένη ημερομηνία
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookedAt >= :date")
    long countByBookedAtAfter(@Param("date") LocalDateTime date);

    // Υπολογισμός συνολικών εσόδων από επιβεβαιωμένες κρατήσεις
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal sumTotalPrice();

    // Αναζήτηση 5 πιο πρόσφατων κρατήσεων
    List<Booking> findTop5ByOrderByBookedAtDesc();
}

