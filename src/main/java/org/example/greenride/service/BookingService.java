package org.example.greenride.service;

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

    // CREATE booking
    public Booking createBooking(Long rideId, Long passengerId, int seatsRequested) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        // Μόνο προγραμματισμένα δρομολόγια
        if (!"PLANNED".equalsIgnoreCase(ride.getStatus())) {
            throw new IllegalStateException("Only PLANNED rides can be booked");
        }

        if (seatsRequested <= 0) {
            throw new IllegalArgumentException("Seats requested must be > 0");
        }

        if (ride.getAvailableSeatsRemain() < seatsRequested) {
            throw new IllegalStateException("Not enough available seats");
        }

        // Ο οδηγός δεν μπορεί να κλείσει θέση στο δικό του ride
        if (ride.getDriver().getId().equals(passenger.getId())) {
            throw new IllegalStateException("Driver cannot book own ride");
        }

        // Έλεγχος διπλής κράτησης για ίδιο ride + passenger
        if (bookingRepository.existsByRideIdAndPassengerId(rideId, passengerId)) {
            throw new IllegalStateException("Passenger already has a booking for this ride");
        }

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setSeatsBooked(seatsRequested);

        // Υπολογισμός συνολικής τιμής
        BigDecimal pricePerSeat = ride.getPricePerSeat();
        BigDecimal totalPrice = pricePerSeat.multiply(BigDecimal.valueOf(seatsRequested));
        booking.setTotalPrice(totalPrice);

        booking.setStatus("ACTIVE");
        booking.setBookedAt(LocalDateTime.now());
        booking.setCancelledAt(null);

        // Μείωση διαθέσιμων θέσεων στο Ride
        ride.setAvailableSeatsRemain(
                ride.getAvailableSeatsRemain() - seatsRequested
        );
        rideRepository.save(ride);

        return bookingRepository.save(booking);
    }

    // CANCEL booking (λογική ακύρωση με επιστροφή θέσεων)
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!"ACTIVE".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalStateException("Only ACTIVE bookings can be cancelled");
        }

        Ride ride = booking.getRide();

        // Επιστροφή θέσεων στο δρομολόγιο
        ride.setAvailableSeatsRemain(
                ride.getAvailableSeatsRemain() + booking.getSeatsBooked()
        );
        rideRepository.save(ride);

        booking.setStatus("CANCELLED");
        booking.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    // READ (ένα)
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    // READ (όλα)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // READ (ανά ride)
    public List<Booking> getBookingsByRide(Long rideId) {
        return bookingRepository.findByRideId(rideId);
    }

    // READ (ανά passenger)
    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    // Πραγματικό DELETE (αν το χρειαστείς – συνήθως κρατάμε bookings για ιστορικό)
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }
}
