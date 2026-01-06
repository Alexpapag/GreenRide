package org.example.greenride.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.greenride.dto.geo.GeoLocationResponseDTO;
import org.example.greenride.service.GeoLocationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geo")
@Tag(name = "Geolocation", description = "Geocoding via OpenStreetMap Nominatim")
public class GeoController {

    private final GeoLocationService geoLocationService;

    public GeoController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping("/forward")
    @Operation(summary = "Forward geocoding", description = "Μετατρέπει διεύθυνση/πόλη σε συντεταγμένες (lat/lon).")
    public GeoLocationResponseDTO forward(@RequestParam("q") String query) {
        return geoLocationService.forwardGeocode(query);
    }

    @PostMapping("/route")  // Or GET with params
    @Operation(summary = "Get route details", description = "Distance, time, polyline for map")
    public GeoLocationService.RouteDetails getRoute(@RequestParam String from, @RequestParam String to) {
        return geoLocationService.getRouteDetails(from, to);
    }
}
