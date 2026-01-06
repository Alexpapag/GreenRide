package org.example.greenride.mapper;

import org.example.greenride.dto.ride.RideRequestDTO;
import org.example.greenride.dto.ride.RideResponseDTO;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;

public final class RideMapper {
    private RideMapper() {}

    public static RideResponseDTO toResponseDTO(Ride r) {
        if (r == null) return null;

        RideResponseDTO dto = new RideResponseDTO();
        dto.setId(r.getId());

        if (r.getDriver() != null) {
            dto.setDriverId(r.getDriver().getId());
            dto.setDriverUsername(r.getDriver().getUsername());
            dto.setDriverFullName(r.getDriver().getFullName());
        }

        dto.setStartDatetime(r.getStartDatetime());
        dto.setEndDatetime(r.getEndDatetime());

        dto.setFromCity(r.getFromCity());
        dto.setFromAddress(r.getFromAddress());
        dto.setToCity(r.getToCity());
        dto.setToAddress(r.getToAddress());

        dto.setDistanceKm(r.getDistanceKm());
        dto.setEstimatedDurationMin(r.getEstimatedDurationMin());

        dto.setAvailableSeatsTotal(r.getAvailableSeatsTotal());
        dto.setAvailableSeatsRemain(r.getAvailableSeatsRemain());

        dto.setPricePerSeat(r.getPricePerSeat());
        dto.setStatus(r.getStatus());
        dto.setWeatherSummary(r.getWeatherSummary());
        dto.setCreatedAt(r.getCreatedAt());

        return dto;
    }

    /** Για create: περνάς driver entity από service */
    public static Ride fromRequestDTO(RideRequestDTO dto, User driver) {
        Ride r = new Ride();
        applyToEntity(dto, driver, r);
        return r;
    }

    /** Για update: ενημερώνει fields στο υπάρχον entity */
    public static void applyToEntity(RideRequestDTO dto, User driver, Ride r) {
        r.setDriver(driver);

        r.setStartDatetime(dto.getStartDatetime());
        r.setEndDatetime(dto.getEndDatetime());

        r.setFromCity(dto.getFromCity());
        r.setFromAddress(dto.getFromAddress());
        r.setToCity(dto.getToCity());
        r.setToAddress(dto.getToAddress());

        r.setDistanceKm(dto.getDistanceKm());
        r.setEstimatedDurationMin(dto.getEstimatedDurationMin());

        r.setAvailableSeatsTotal(dto.getAvailableSeatsTotal());
        // αν θες στο create: remain = total (service καλύτερα)
        r.setPricePerSeat(dto.getPricePerSeat());
    }
}

