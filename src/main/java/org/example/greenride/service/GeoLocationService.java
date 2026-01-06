package org.example.greenride.service;

import org.example.greenride.dto.geo.GeoLocationResponseDTO;

public interface GeoLocationService {
    GeoLocationResponseDTO forwardGeocode(String query);
    RouteDetails getRouteDetails(String from, String to);
    RouteDetails getRouteDetailsByCoordinates(double fromLat, double fromLon, double toLat, double toLon);

    // Record/DTO for response
    public record RouteDetails(Double distanceKm, Integer durationMinutes, String polyline) {}

}
