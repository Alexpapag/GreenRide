package org.example.greenride.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.greenride.dto.geo.AthensLocation;
import org.example.greenride.dto.geo.GeoLocationResponseDTO;
import org.example.greenride.service.AthensLocationService;
import org.example.greenride.service.GeoLocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API Controller για γεωγραφικές πληροφορίες (geocoding & routing)
@RestController
@RequestMapping("/geo")
@Tag(name = "Geolocation", description = "Geocoding via OpenStreetMap Nominatim")
public class GeoController {

    private final GeoLocationService geoLocationService;
    private final AthensLocationService athensLocationService;

    public GeoController(GeoLocationService geoLocationService, AthensLocationService athensLocationService) {
        this.geoLocationService = geoLocationService;
        this.athensLocationService = athensLocationService;
    }

    // Μετατροπή διεύθυνσης σε συντεταγμένες (forward geocoding)
    @GetMapping("/forward")
    @Operation(summary = "Forward geocoding", description = "Μετατρέπει διεύθυνση/πόλη σε συντεταγμένες (lat/lon).")
    public GeoLocationResponseDTO forward(@RequestParam("q") String query) {
        return geoLocationService.forwardGeocode(query);
    }

    // Υπολογισμός διαδρομής (απόσταση, χρόνος)
    @GetMapping("/route")
    @Operation(summary = "Get route details", description = "Distance, time, polyline for map")
    public GeoLocationService.RouteDetails getRoute(@RequestParam String from, @RequestParam String to) {
        return geoLocationService.getRouteDetails(from, to);
    }

    // Test endpoint για geocoding
    @GetMapping("/test")
    @Operation(summary = "Test geocoding", description = "Test endpoint to debug geocoding")
    public String testGeocode(@RequestParam(defaultValue = "Athens, Greece") String q) {
        try {
            GeoLocationResponseDTO result = geoLocationService.forwardGeocode(q);
            return "Success: " + result.getDisplayName() + " at " + result.getLat() + ", " + result.getLon();
        } catch (Exception e) {
            return "Error: " + e.getMessage() + "\n\nStack trace: " + e.toString();
        }
    }

    // Ανάκτηση προκαθορισμένων τοποθεσιών Αθήνας
    @GetMapping("/athens/locations")
    @Operation(summary = "Get Athens locations", description = "Get list of predefined Athens locations")
    public List<AthensLocation> getAthensLocations(@RequestParam(required = false) String q) {
        if (q != null && !q.trim().isEmpty()) {
            return athensLocationService.searchLocations(q);
        }
        return athensLocationService.getAllLocations();
    }

    // Γρήγορος υπολογισμός διαδρομής μεταξύ τοποθεσιών Αθήνας
    @GetMapping("/athens/route")
    @Operation(summary = "Calculate route between Athens locations", description = "Fast route calculation using predefined coordinates")
    public GeoLocationService.RouteDetails getAthensRoute(@RequestParam String from, @RequestParam String to) {
        var fromLoc = athensLocationService.getLocation(from);
        var toLoc = athensLocationService.getLocation(to);

        if (fromLoc.isPresent() && toLoc.isPresent()) {
            return geoLocationService.getRouteDetailsByCoordinates(
                fromLoc.get().getLat(), fromLoc.get().getLon(),
                toLoc.get().getLat(), toLoc.get().getLon()
            );
        } else {
            return geoLocationService.getRouteDetails(from, to);
        }
    }
}
