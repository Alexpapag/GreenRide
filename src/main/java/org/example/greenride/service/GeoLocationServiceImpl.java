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
}
