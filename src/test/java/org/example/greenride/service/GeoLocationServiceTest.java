package org.example.greenride.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.greenride.dto.geo.GeoLocationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeoLocationServiceTest {

    @Autowired
    private GeoLocationService geoLocationService;

    @Test
    void testForwardGeocode_ValidCity() {
        // Test geocoding a well-known city
        String query = "Athens, Greece";

        GeoLocationResponseDTO result = geoLocationService.forwardGeocode(query);

        assertNotNull(result);
        assertNotNull(result.getLat());
        assertNotNull(result.getLon());
        assertNotNull(result.getDisplayName());

        // Athens should be roughly at these coordinates
        assertTrue(result.getLat() > 37.0 && result.getLat() < 38.5);
        assertTrue(result.getLon() > 23.0 && result.getLon() < 24.5);

        System.out.println("Geocoded: " + query);
        System.out.println("Result: " + result.getDisplayName());
        System.out.println("Coordinates: " + result.getLat() + ", " + result.getLon());
    }

    @Test
    void testForwardGeocode_InvalidCity() {
        // Test with invalid/non-existent location
        String query = "XYZ123NonExistentCity";

        assertThrows(IllegalArgumentException.class, () -> {
            geoLocationService.forwardGeocode(query);
        });
    }

    @Test
    void testGetRouteDetails_ValidRoute() {
        // Test route calculation between two cities
        String from = "Athens, Greece";
        String to = "Thessaloniki, Greece";

        GeoLocationService.RouteDetails result = geoLocationService.getRouteDetails(from, to);

        assertNotNull(result);
        assertNotNull(result.distanceKm());
        assertNotNull(result.durationMinutes());
        assertNotNull(result.polyline());

        // Athens to Thessaloniki is roughly 500km
        assertTrue(result.distanceKm() > 400 && result.distanceKm() < 600);

        // Should take several hours
        assertTrue(result.durationMinutes() > 200);

        System.out.println("Route: " + from + " -> " + to);
        System.out.println("Distance: " + result.distanceKm() + " km");
        System.out.println("Duration: " + result.durationMinutes() + " minutes");
        System.out.println("Polyline length: " + result.polyline().length() + " chars");
    }

    @Test
    void testGetRouteDetails_ShortRoute() {
        // Test a shorter route
        String from = "Paris, France";
        String to = "Versailles, France";

        GeoLocationService.RouteDetails result = geoLocationService.getRouteDetails(from, to);

        assertNotNull(result);
        assertTrue(result.distanceKm() > 0);
        assertTrue(result.distanceKm() < 50); // Should be less than 50km

        System.out.println("Short route: " + from + " -> " + to);
        System.out.println("Distance: " + result.distanceKm() + " km");
        System.out.println("Duration: " + result.durationMinutes() + " minutes");
    }

    @Test
    void testGetRouteDetails_InvalidRoute() {
        // Test with invalid locations
        String from = "XYZ123NonExistent";
        String to = "ABC456NonExistent";

        assertThrows(RuntimeException.class, () -> {
            geoLocationService.getRouteDetails(from, to);
        });
    }
}
