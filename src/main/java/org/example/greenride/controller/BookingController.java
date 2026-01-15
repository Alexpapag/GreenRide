package org.example.greenride.controller;

import jakarta.validation.Valid;
import org.example.greenride.dto.booking.BookingRequestDTO;
import org.example.greenride.dto.booking.BookingResponseDTO;
import org.example.greenride.entity.Booking;
import org.example.greenride.mapper.BookingMapper;
import org.example.greenride.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API Controller για διαχείριση κρατήσεων (bookings)
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Δημιουργία νέας κράτησης
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        Booking created = bookingService.createBooking(dto);
        return BookingMapper.toResponseDTO(created);
    }

    // Ανάκτηση μιας κράτησης
    @GetMapping("/{id}")
    public BookingResponseDTO getBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return BookingMapper.toResponseDTO(booking);
    }

    // Ανάκτηση όλων των κρατήσεων
    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings()
                .stream()
                .map(BookingMapper::toResponseDTO)
                .toList();
    }

    // Ανάκτηση κρατήσεων για συγκεκριμένη διαδρομή
    @GetMapping("/ride/{rideId}")
    public List<BookingResponseDTO> getBookingsByRide(@PathVariable Long rideId) {
        return bookingService.getBookingsByRideId(rideId)
                .stream()
                .map(BookingMapper::toResponseDTO)
                .toList();
    }

    // Ανάκτηση κρατήσεων για συγκεκριμένο επιβάτη
    @GetMapping("/passenger/{passengerId}")
    public List<BookingResponseDTO> getBookingsByPassenger(@PathVariable Long passengerId) {
        return bookingService.getBookingsByPassengerId(passengerId)
                .stream()
                .map(BookingMapper::toResponseDTO)
                .toList();
    }

    // Ακύρωση κράτησης
    @PutMapping("/{id}/cancel")
    public BookingResponseDTO cancelBooking(@PathVariable Long id) {
        Booking cancelled = bookingService.cancelBooking(id);
        return BookingMapper.toResponseDTO(cancelled);
    }

    // Διαγραφή κράτησης
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}

