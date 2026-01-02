package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteeTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class RouteeAuthServiceImpl implements RouteeAuthService {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final String appId;
    private final String appSecret;

    private String cachedToken;
    private Instant cachedExpiresAt;

    public RouteeAuthServiceImpl(
            RestTemplate restTemplate,
            @Value("${routee.auth-url}") String authUrl,
            @Value("${routee.app-id}") String appId,
            @Value("${routee.app-secret}") String appSecret
    ) {
        this.restTemplate = restTemplate;
        this.authUrl = authUrl;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public synchronized String getAccessToken() {
        if (cachedToken != null && cachedExpiresAt != null && Instant.now().isBefore(cachedExpiresAt)) {
            return cachedToken;
        }

        String basic = Base64.getEncoder()
                .encodeToString((appId + ":" + appSecret).getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basic);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ResponseEntity<RouteeTokenResponseDTO> res = restTemplate.exchange(
                authUrl, HttpMethod.POST, entity, RouteeTokenResponseDTO.class
        );

        RouteeTokenResponseDTO body = res.getBody();
        if (body == null || body.getAccessToken() == null || body.getAccessToken().isBlank()) {
            throw new IllegalStateException("Routee token endpoint returned empty token");
        }

        cachedToken = body.getAccessToken();
        long expiresIn = body.getExpiresIn() != null ? body.getExpiresIn() : 3600L;
        cachedExpiresAt = Instant.now().plusSeconds(Math.max(60, expiresIn - 60));

        return cachedToken;
    }
}

