package org.example.greenride.controller;

import org.example.greenride.entity.Ride;
import org.example.greenride.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // CREATE ride για συγκεκριμένο driver (driverId ως request param)
    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestParam Long driverId,
                                           @RequestBody Ride rideRequest) {
        Ride created = rideService.createRide(driverId, rideRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL
    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Ride getRideById(@PathVariable Long id) {
        return rideService.getRideById(id);
    }

    // UPDATE (partial update, όπως στο service)
    @PutMapping("/{id}")
    public Ride updateRide(@PathVariable Long id,
                           @RequestBody Ride rideRequest) {
        return rideService.updateRide(id, rideRequest);
    }

    // CANCEL (λογική ακύρωση -> status = CANCELLED)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRide(@PathVariable Long id) {
        rideService.cancelRide(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE (πραγματικό delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }

    // SEARCH: by cities
    @GetMapping("/search/by-cities")
    public List<Ride> searchByCities(@RequestParam String fromCity,
                                     @RequestParam String toCity) {
        return rideService.searchByCities(fromCity, toCity);
    }

    // SEARCH: by cities & status
    @GetMapping("/search/by-cities-and-status")
    public List<Ride> searchByCitiesAndStatus(@RequestParam String fromCity,
                                              @RequestParam String toCity,
                                              @RequestParam String status) {
        return rideService.searchByCitiesAndStatus(fromCity, toCity, status);
    }

    // SEARCH: by start datetime range
    @GetMapping("/search/by-start-range")
    public List<Ride> searchByStartRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startFrom,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTo) {

        return rideService.searchByStartDatetimeRange(startFrom, startTo);
    }
}
