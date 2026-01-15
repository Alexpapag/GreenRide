package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteePhoneValidationResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

// Service για επικύρωση τηλεφώνων μέσω Routee Phone Validation API
@Service
public class RouteePhoneValidationServiceImpl implements RouteePhoneValidationService {

    private final RestTemplate restTemplate;
    private final RouteeAuthService authService;
    private final String baseUrl;

    public RouteePhoneValidationServiceImpl(
            RestTemplate restTemplate,
            RouteeAuthService authService,
            @Value("${routee.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.baseUrl = baseUrl;
    }

    // Επικύρωση τηλεφώνου σε E.164 format μέσω Routee lookup API
    @Override
    public RouteePhoneValidationResponseDTO validate(String phoneE164) {
        // Το endpoint είναι αυτό που περιγράφει το Routee docs για phone validation :contentReference[oaicite:2]{index=2}
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/lookup/phones/")
                .path(phoneE164)
                .toUriString();

        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<RouteePhoneValidationResponseDTO> res = restTemplate.exchange(
                url, HttpMethod.GET, entity, RouteePhoneValidationResponseDTO.class
        );

        if (res.getBody() == null) {
            throw new IllegalStateException("Routee phone validation returned empty body");
        }
        return res.getBody();
    }
}
