package org.example.greenride.service.external;

import org.example.greenride.dto.external.JsonPlaceholderPostRequestDTO;
import org.example.greenride.dto.external.JsonPlaceholderPostResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalPostServiceImpl implements ExternalPostService {

    private static final String JSONPLACEHOLDER_POSTS_URL = "https://jsonplaceholder.typicode.com/posts";

    private final RestTemplate restTemplate;

    public ExternalPostServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JsonPlaceholderPostResponseDTO createPost(JsonPlaceholderPostRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<JsonPlaceholderPostRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<JsonPlaceholderPostResponseDTO> response = restTemplate.exchange(
                JSONPLACEHOLDER_POSTS_URL,
                HttpMethod.POST,
                entity,
                JsonPlaceholderPostResponseDTO.class
        );

        JsonPlaceholderPostResponseDTO body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("External API returned empty response body");
        }
        return body;
    }
}
