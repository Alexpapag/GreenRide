package org.example.greenride.dto.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingRequestDTO {
// DTO για αίτημα δημιουργίας κράτησης

    @NotNull
    private Long rideId; // ID διαδρομής

    @NotNull
    private Long passengerId; // ID επιβάτη

    @Min(1)
    private int seatsRequested; // Αριθμός θέσεων

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public int getSeatsRequested() { return seatsRequested; }
    public void setSeatsRequested(int seatsRequested) { this.seatsRequested = seatsRequested; }
}
