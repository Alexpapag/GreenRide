package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteeSmsAnalyzeRequestDTO;
import org.example.greenride.dto.routee.RouteeSmsAnalyzeResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RouteeMessagingServiceImpl implements RouteeMessagingService {

    private final RestTemplate restTemplate;
    private final RouteeAuthService routeeAuthService;
    private final String baseUrl;

    public RouteeMessagingServiceImpl(
            RestTemplate restTemplate,
            RouteeAuthService routeeAuthService,
            @Value("${routee.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.routeeAuthService = routeeAuthService;
        this.baseUrl = baseUrl;
    }

    @Override
    public RouteeSmsAnalyzeResponseDTO analyzeSms(RouteeSmsAnalyzeRequestDTO request) {
        // Συνήθως είναι /sms/analyze (αν στο δικό σου docs είναι αλλιώς, αλλάζεις μόνο αυτό το path)
        String url = baseUrl + "/sms/analyze";

        String token = routeeAuthService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<RouteeSmsAnalyzeRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RouteeSmsAnalyzeResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                RouteeSmsAnalyzeResponseDTO.class
        );

        RouteeSmsAnalyzeResponseDTO body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Routee analyze returned empty body");
        }
        return body;
    }
}
