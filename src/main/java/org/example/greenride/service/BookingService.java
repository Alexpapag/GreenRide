package org.example.greenride.service;

import org.example.greenride.dto.booking.BookingRequestDTO;
import org.example.greenride.entity.Booking;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.example.greenride.repository.BookingRepository;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          RideRepository rideRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    // CREATE (DTO)
    public Booking createBooking(BookingRequestDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Booking data is required");

        Ride ride = rideRepository.findById(dto.getRideId())
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        User passenger = userRepository.findById(dto.getPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        int seatsRequested = dto.getSeatsRequested();
        if (seatsRequested <= 0) throw new IllegalArgumentException("Seats requested must be > 0");

        if (ride.getAvailableSeatsRemain() == null || ride.getAvailableSeatsRemain() < seatsRequested) {
            throw new IllegalStateException("Not enough available seats");
        }

        // update seats on ride
        ride.setAvailableSeatsRemain(ride.getAvailableSeatsRemain() - seatsRequested);
        rideRepository.save(ride);

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);

        // στο entity σου το field λέγεται seatsBooked (όχι seatsRequested)
        booking.setSeatsBooked(seatsRequested);

        // totalPrice = pricePerSeat * seatsBooked
        BigDecimal pricePerSeat = ride.getPricePerSeat() == null ? BigDecimal.ZERO : ride.getPricePerSeat();
        booking.setTotalPrice(pricePerSeat.multiply(BigDecimal.valueOf(seatsRequested)));

        // ΣΗΜΑΝΤΙΚΟ: διάλεξε ΕΝΑ status convention
        // Εσύ έχεις αναντιστοιχία ("ACTIVE" vs "PENDING/CONFIRMED/CANCELLED").
        // Κράτα για τώρα:
        booking.setStatus("PENDING");

        booking.setBookedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByRideId(Long rideId) {
        // Αν δεν έχεις αυτό το method στο repository, δες σημείωση πιο κάτω
        return bookingRepository.findByRideId(rideId);
    }

    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            return booking;
        }

        // επιστρέφουμε seats στο ride
        Ride ride = booking.getRide();
        if (ride != null && booking.getSeatsBooked() != null) {
            Integer remain = ride.getAvailableSeatsRemain() == null ? 0 : ride.getAvailableSeatsRemain();
            ride.setAvailableSeatsRemain(remain + booking.getSeatsBooked());
            rideRepository.save(ride);
        }

        booking.setStatus("CANCELLED");
        booking.setCancelledAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }
}

