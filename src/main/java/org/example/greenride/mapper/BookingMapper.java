package org.example.greenride.mapper;

import org.example.greenride.dto.booking.BookingResponseDTO;
import org.example.greenride.entity.Booking;

// Mapper για μετατροπή Booking entity σε BookingResponseDTO
public final class BookingMapper {
    private BookingMapper() {}

    // Μετατροπή από Booking entity σε BookingResponseDTO
    public static BookingResponseDTO toResponseDTO(Booking b) {
        if (b == null) return null;

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId());

        if (b.getRide() != null) dto.setRideId(b.getRide().getId());
        if (b.getPassenger() != null) dto.setPassengerId(b.getPassenger().getId());

        dto.setSeatsBooked(b.getSeatsBooked());
        dto.setTotalPrice(b.getTotalPrice());

        dto.setStatus(b.getStatus());
        dto.setBookedAt(b.getBookedAt());
        dto.setCancelledAt(b.getCancelledAt());

        return dto;
    }
}
