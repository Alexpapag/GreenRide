package org.example.greenride.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.greenride.dto.geo.GeoLocationResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

    private static final String NOMINATIM_SEARCH_URL = "https://nominatim.openstreetmap.org/search";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeoLocationServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Μικρό DTO για το Nominatim response
    public static class NominatimResult {
        public String lat;
        public String lon;
        public String display_name;
    }

    @Override
    public GeoLocationResponseDTO forwardGeocode(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_SEARCH_URL)
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .queryParam("limit", "1")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        // Nominatim απαιτεί User-Agent. Βάλε κάτι “project specific”
        headers.set(HttpHeaders.USER_AGENT, "GreenRide/1.0 (contact: team@greenride.local)");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<NominatimResult>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<NominatimResult> results = response.getBody();
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("No geolocation results for query: " + query);
        }

        NominatimResult r = results.get(0);
        Double lat = Double.valueOf(r.lat);
        Double lon = Double.valueOf(r.lon);

        return new GeoLocationResponseDTO(lat, lon, r.display_name);
    }
    private static final String OSRM_ROUTE_URL = "https://router.project-osrm.org/route/v1/driving/";

    @Override
    public RouteDetails getRouteDetails(String from, String to) {
        try {
            GeoLocationResponseDTO fromLoc = forwardGeocode(from);
            GeoLocationResponseDTO toLoc = forwardGeocode(to);

            return getRouteDetailsByCoordinates(fromLoc.getLat(), fromLoc.getLon(), toLoc.getLat(), toLoc.getLon());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get route details: " + e.getMessage(), e);
        }
    }

    @Override
    public RouteDetails getRouteDetailsByCoordinates(double fromLat, double fromLon, double toLat, double toLon) {
        try {
            String coords = fromLon + "," + fromLat + ";" + toLon + "," + toLat;
            String url = UriComponentsBuilder.fromHttpUrl(OSRM_ROUTE_URL + coords)
                    .queryParam("overview", "full")  // Polyline geometry
                    .queryParam("alternatives", "false")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "GreenRide/1.0 (contact: team@greenride.local)");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Parse JSON response
            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.has("routes") && root.get("routes").size() > 0) {
                JsonNode route = root.get("routes").get(0);

                // Distance in meters -> convert to km
                double distanceMeters = route.get("distance").asDouble();
                double distanceKm = Math.round(distanceMeters / 1000.0 * 100.0) / 100.0;

                // Duration in seconds -> convert to minutes
                double durationSeconds = route.get("duration").asDouble();
                int durationMin = (int) Math.ceil(durationSeconds / 60.0);

                // Get polyline geometry
                String polyline = route.get("geometry").asText();

                return new RouteDetails(distanceKm, durationMin, polyline);
            } else {
                throw new IllegalArgumentException("No route found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get route details: " + e.getMessage(), e);
        }
    }

}
