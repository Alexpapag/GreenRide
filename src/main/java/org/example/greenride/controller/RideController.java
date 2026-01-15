package org.example.greenride.controller;

import jakarta.validation.Valid;
import org.example.greenride.dto.ride.RideRequestDTO;
import org.example.greenride.dto.ride.RideResponseDTO;
import org.example.greenride.entity.Ride;
import org.example.greenride.mapper.RideMapper;
import org.example.greenride.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API Controller για διαχείριση διαδρομών (rides)
@RestController
@RequestMapping("/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Δημιουργία νέας διαδρομής
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponseDTO createRide(@Valid @RequestBody RideRequestDTO dto) {
        Ride created = rideService.createRide(dto);
        return RideMapper.toResponseDTO(created);
    }

    // Ανάκτηση μιας διαδρομής
    @GetMapping("/{id}")
    public RideResponseDTO getRide(@PathVariable Long id) {
        Ride ride = rideService.getRideById(id);
        return RideMapper.toResponseDTO(ride);
    }

    // Ανάκτηση όλων των διαδρομών
    @GetMapping
    public List<RideResponseDTO> getAllRides() {
        return rideService.getAllRides()
                .stream()
                .map(RideMapper::toResponseDTO)
                .toList();
    }

    // Ενημέρωση διαδρομής
    @PutMapping("/{id}")
    public RideResponseDTO updateRide(@PathVariable Long id,
                                      @RequestBody RideRequestDTO dto) {
        Ride updated = rideService.updateRide(id, dto);
        return RideMapper.toResponseDTO(updated);
    }

    // Διαγραφή διαδρομής
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
    }
}


