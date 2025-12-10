package org.example.greenride.controller;
import org.example.greenride.entity.Booking;
import org.example.greenride.service.BookingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {  //:contentReference[oaicite:0]{index=0}

    private final BookingService bookingService;  // :contentReference[oaicite:1]{index=1}

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // CREATE booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest request) {
        Booking created = bookingService.createBooking(
                request.getRideId(),
                request.getPassengerId(),
                request.getSeatsRequested()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // CANCEL booking (logical cancel + return seats)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    // READ one
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    // READ all
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // READ by ride
    @GetMapping("/by-ride/{rideId}")
    public List<Booking> getBookingsByRide(@PathVariable Long rideId) {
        return bookingService.getBookingsByRide(rideId);
    }

    // READ by passenger
    @GetMapping("/by-passenger/{passengerId}")
    public List<Booking> getBookingsByPassenger(@PathVariable Long passengerId) {
        return bookingService.getBookingsByPassenger(passengerId);
    }

    // HARD DELETE (if you actually want to remove it)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- DTO ----------

    // Simple DTO for createBooking
    public static class CreateBookingRequest {
        private Long rideId;
        private Long passengerId;
        private int seatsRequested;

        public Long getRideId() {
            return rideId;
        }

        public void setRideId(Long rideId) {
            this.rideId = rideId;
        }

        public Long getPassengerId() {
            return passengerId;
        }

        public void setPassengerId(Long passengerId) {
            this.passengerId = passengerId;
        }

        public int getSeatsRequested() {
            return seatsRequested;
        }

        public void setSeatsRequested(int seatsRequested) {
            this.seatsRequested = seatsRequested;
        }
    }
}
