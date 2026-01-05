package org.example.greenride.repository;

import org.example.greenride.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRideId(Long rideId);

    List<Booking> findByPassengerId(Long passengerId);

    List<Booking> findByPassengerIdAndStatus(Long passengerId, String status);

    boolean existsByRideIdAndPassengerId(Long rideId, Long passengerId);

    // New methods for AdminService
    long countByStatus(String status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookedAt >= :date")
    long countByBookedAtAfter(@Param("date") LocalDateTime date);

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal sumTotalPrice();

    List<Booking> findTop5ByOrderByBookedAtDesc();
}

