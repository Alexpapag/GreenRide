package org.example.greenride.service;

import org.example.greenride.dto.ride.RideRequestDTO;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.example.greenride.mapper.RideMapper;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideService(RideRepository rideRepository,
                       UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE (DTO)
    // =========================
    public Ride createRide(RideRequestDTO dto) {
        System.out.println("=== RIDE SERVICE: createRide ===");
        System.out.println("DTO received: " + dto);

        if (dto == null) {
            System.out.println("Error: Ride data is null");
            throw new IllegalArgumentException("Ride data is required");
        }

        // Βρες driver
        if (dto.getDriverId() == null) {
            System.out.println("Error: Driver id is null");
            throw new IllegalArgumentException("Driver id is required");
        }

        System.out.println("Looking for driver with ID: " + dto.getDriverId());
        User driver = userRepository.findById(dto.getDriverId())
                .orElseThrow(() -> {
                    System.out.println("Error: Driver not found with ID: " + dto.getDriverId());
                    return new IllegalArgumentException("Driver not found");
                });
        System.out.println("Found driver: " + driver.getUsername());

        // Βασικές επικυρώσεις
        if (dto.getDriverId() == null) {
            throw new IllegalArgumentException("Driver id is required");
        }

        if (dto.getStartDatetime() == null) {
            System.out.println("Error: Start datetime is null");
            throw new IllegalArgumentException("Start datetime is required");
        }
        if (dto.getFromCity() == null || dto.getToCity() == null) {
            System.out.println("Error: Cities are null");
            throw new IllegalArgumentException("From city and To city are required");
        }
        if (dto.getAvailableSeatsTotal() == null || dto.getAvailableSeatsTotal() <= 0) {
            System.out.println("Error: Invalid seats: " + dto.getAvailableSeatsTotal());
            throw new IllegalArgumentException("Available seats must be > 0");
        }
        if (dto.getPricePerSeat() == null || dto.getPricePerSeat().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: Invalid price: " + dto.getPricePerSeat());
            throw new IllegalArgumentException("Price per seat must be > 0");
        }

        System.out.println("All validations passed, creating ride entity...");

        Ride ride = RideMapper.fromRequestDTO(dto, driver);

        // στην αρχή όλα τα seats διαθέσιμα
        ride.setAvailableSeatsRemain(dto.getAvailableSeatsTotal());

        // αρχικό status
        ride.setStatus("PLANNED");

        // createdAt
        ride.setCreatedAt(LocalDateTime.now());

        System.out.println("Ride entity created, saving to database...");
        Ride saved = rideRepository.save(ride);
        System.out.println("Ride saved with ID: " + saved.getId());

        return saved;
    }

    // =========================
    // CREATE (παλιό, entity) - το κρατάμε για backward compatibility
    // =========================
    public Ride createRide(Long driverId, Ride rideData) {
        // Βρες driver
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        // Βασικές επικυρώσεις
        if (rideData.getStartDatetime() == null) {
            throw new IllegalArgumentException("Start datetime is required");
        }
        if (rideData.getFromCity() == null || rideData.getToCity() == null) {
            throw new IllegalArgumentException("From city and To city are required");
        }
        if (rideData.getAvailableSeatsTotal() == null || rideData.getAvailableSeatsTotal() <= 0) {
            throw new IllegalArgumentException("Available seats must be > 0");
        }
        if (rideData.getPricePerSeat() == null
                || rideData.getPricePerSeat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per seat must be > 0");
        }

        Ride ride = new Ride();
        ride.setDriver(driver);

        ride.setStartDatetime(rideData.getStartDatetime());
        ride.setEndDatetime(rideData.getEndDatetime());

        ride.setFromCity(rideData.getFromCity());
        ride.setFromAddress(rideData.getFromAddress());
        ride.setToCity(rideData.getToCity());
        ride.setToAddress(rideData.getToAddress());

        ride.setDistanceKm(rideData.getDistanceKm());
        ride.setEstimatedDurationMin(rideData.getEstimatedDurationMin());

        ride.setAvailableSeatsTotal(rideData.getAvailableSeatsTotal());
        // στην αρχή όλα τα seats διαθέσιμα
        ride.setAvailableSeatsRemain(rideData.getAvailableSeatsTotal());

        ride.setPricePerSeat(rideData.getPricePerSeat());

        // αρχικό status
        ride.setStatus("PLANNED");

        ride.setWeatherSummary(rideData.getWeatherSummary());

        ride.setCreatedAt(LocalDateTime.now());

        return rideRepository.save(ride);
    }

    // =========================
    // READ (ένα)
    // =========================
    public Ride getRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));
    }

    // =========================
    // READ (όλα)
    // =========================
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    // =========================
    // UPDATE (DTO) - partial update
    // =========================
    public Ride updateRide(Long id, RideRequestDTO dto) {
        Ride existing = getRideById(id);

        // Completed / cancelled δεν αλλάζουν
        if ("COMPLETED".equalsIgnoreCase(existing.getStatus())
                || "CANCELLED".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Completed or cancelled ride cannot be updated");
        }

        // driver change (αν δώσεις driverId)
        if (dto.getDriverId() != null) {
            User driver = userRepository.findById(dto.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
            existing.setDriver(driver);
        }

        if (dto.getStartDatetime() != null) existing.setStartDatetime(dto.getStartDatetime());
        if (dto.getEndDatetime() != null) existing.setEndDatetime(dto.getEndDatetime());

        if (dto.getFromCity() != null) existing.setFromCity(dto.getFromCity());
        if (dto.getFromAddress() != null) existing.setFromAddress(dto.getFromAddress());

        if (dto.getToCity() != null) existing.setToCity(dto.getToCity());
        if (dto.getToAddress() != null) existing.setToAddress(dto.getToAddress());

        if (dto.getDistanceKm() != null) existing.setDistanceKm(dto.getDistanceKm());
        if (dto.getEstimatedDurationMin() != null) existing.setEstimatedDurationMin(dto.getEstimatedDurationMin());

        if (dto.getAvailableSeatsTotal() != null && dto.getAvailableSeatsTotal() > 0) {
            int newTotal = dto.getAvailableSeatsTotal();
            int oldRemain = existing.getAvailableSeatsRemain();

            existing.setAvailableSeatsTotal(newTotal);
            // αν τα remaining είναι παραπάνω από τα νέα total, τα μειώνουμε
            if (oldRemain > newTotal) {
                existing.setAvailableSeatsRemain(newTotal);
            }
        }

        if (dto.getPricePerSeat() != null && dto.getPricePerSeat().compareTo(BigDecimal.ZERO) > 0) {
            existing.setPricePerSeat(dto.getPricePerSeat());
        }

        return rideRepository.save(existing);
    }

    // =========================
    // UPDATE (παλιό, entity) - partial update
    // =========================
    public Ride updateRide(Long id, Ride updated) {
        Ride existing = getRideById(id);

        // Completed / cancelled δεν αλλάζουν
        if ("COMPLETED".equalsIgnoreCase(existing.getStatus())
                || "CANCELLED".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Completed or cancelled ride cannot be updated");
        }

        if (updated.getStartDatetime() != null) {
            existing.setStartDatetime(updated.getStartDatetime());
        }
        if (updated.getEndDatetime() != null) {
            existing.setEndDatetime(updated.getEndDatetime());
        }
        if (updated.getFromCity() != null) {
            existing.setFromCity(updated.getFromCity());
        }
        if (updated.getFromAddress() != null) {
            existing.setFromAddress(updated.getFromAddress());
        }
        if (updated.getToCity() != null) {
            existing.setToCity(updated.getToCity());
        }
        if (updated.getToAddress() != null) {
            existing.setToAddress(updated.getToAddress());
        }
        if (updated.getDistanceKm() != null) {
            existing.setDistanceKm(updated.getDistanceKm());
        }
        if (updated.getEstimatedDurationMin() != null) {
            existing.setEstimatedDurationMin(updated.getEstimatedDurationMin());
        }

        if (updated.getAvailableSeatsTotal() != null && updated.getAvailableSeatsTotal() > 0) {
            int newTotal = updated.getAvailableSeatsTotal();
            int oldRemain = existing.getAvailableSeatsRemain();
            existing.setAvailableSeatsTotal(newTotal);
            // αν τα remaining είναι παραπάνω από τα νέα total, τα μειώνουμε
            if (oldRemain > newTotal) {
                existing.setAvailableSeatsRemain(newTotal);
            }
        }

        if (updated.getPricePerSeat() != null
                && updated.getPricePerSeat().compareTo(BigDecimal.ZERO) > 0) {
            existing.setPricePerSeat(updated.getPricePerSeat());
        }

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }

        if (updated.getWeatherSummary() != null) {
            existing.setWeatherSummary(updated.getWeatherSummary());
        }

        return rideRepository.save(existing);
    }

    // =========================
    // CANCEL
    // =========================
    public void cancelRide(Long id) {
        Ride ride = getRideById(id);
        ride.setStatus("CANCELLED");
        rideRepository.save(ride);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteRide(Long id) {
        if (!rideRepository.existsById(id)) {
            throw new IllegalArgumentException("Ride not found");
        }
        rideRepository.deleteById(id);
    }

    // =========================
    // SEARCH
    // =========================
    public List<Ride> searchByCities(String fromCity, String toCity) {
        return rideRepository.findByFromCityAndToCity(fromCity, toCity);
    }

    public List<Ride> searchByCitiesAndStatus(String fromCity, String toCity, String status) {
        return rideRepository.findByFromCityAndToCityAndStatus(fromCity, toCity, status);
    }

    public List<Ride> searchByStartDatetimeRange(LocalDateTime startFrom, LocalDateTime startTo) {
        return rideRepository.findByStartDatetimeBetween(startFrom, startTo);
    }

    // Driver's rides (add after getAllRides())
    public List<Ride> getRidesByDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        return rideRepository.findByDriver(driver);
    }

    // Available rides (exclude driver's own)
    public List<Ride> getAvailableRides(Long excludeDriverId) {
        return rideRepository.findByDriverIdNotAndStatus(excludeDriverId, "PLANNED");
    }

}

