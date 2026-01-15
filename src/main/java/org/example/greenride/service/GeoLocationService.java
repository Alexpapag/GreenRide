package org.example.greenride.service;

import org.example.greenride.dto.geo.GeoLocationResponseDTO;

// Service interface για γεωγραφικές λειτουργίες (geocoding, routing)
public interface GeoLocationService {
    // Μετατροπή διεύθυνσης σε συντεταγμένες (forward geocoding)
    GeoLocationResponseDTO forwardGeocode(String query);
    // Υπολογισμός διαδρομής από διεύθυνση σε διεύθυνση
    RouteDetails getRouteDetails(String from, String to);
    // Υπολογισμός διαδρομής από συντεταγμένες σε συντεταγμένες
    RouteDetails getRouteDetailsByCoordinates(double fromLat, double fromLon, double toLat, double toLon);

    // Record/DTO για απάντηση routing (απόσταση, διάρκεια, polyline)
    public record RouteDetails(Double distanceKm, Integer durationMinutes, String polyline) {}

}
