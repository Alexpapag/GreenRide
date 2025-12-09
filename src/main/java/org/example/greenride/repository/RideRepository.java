package org.example.greenride.repository;

import org.example.greenride.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    // Αναζήτηση από πόλη σε πόλη
    List<Ride> findByFromCityAndToCity(String fromCity, String toCity);

    // Αναζήτηση από πόλη σε πόλη + status (π.χ. PLANNED)
    List<Ride> findByFromCityAndToCityAndStatus(String fromCity, String toCity, String status);

    // Αναζήτηση με βάση ημερομηνία/ώρα εκκίνησης
    List<Ride> findByStartDatetimeBetween(LocalDateTime startFrom, LocalDateTime startTo);
}
