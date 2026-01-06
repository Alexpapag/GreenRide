package org.example.greenride.service;

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

    public GeoLocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        GeoLocationResponseDTO fromLoc = forwardGeocode(from);
        GeoLocationResponseDTO toLoc = forwardGeocode(to);

        String coords = fromLoc.getLon() + "," + fromLoc.getLat() + ";" + toLoc.getLon() + "," + toLoc.getLat();
        String url = UriComponentsBuilder.fromHttpUrl(OSRM_ROUTE_URL + coords)
                .queryParam("overview", "full")  // Polyline geometry
                .queryParam("alternatives", "false")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "GreenRide/1.0 (contact: team@greenride.local)");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Parse simple JSON (use ObjectMapper if needed)
        // For brevity, mock parse - real impl extracts routes[0].distance, .duration, .geometry
        // Assume parsed: distance in meters -> km, duration in ms -> min, polyline
        double distanceKm = 15.2;  // Replace with JSON parse: response.getBody()
        int durationMin = 22;
        String polyline = "encoded_polyline_here";  // From geometry

        return new RouteDetails(distanceKm, durationMin, polyline);
    }

}
