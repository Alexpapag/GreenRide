package org.example.greenride.service.external;

import org.example.greenride.dto.external.JsonPlaceholderPostRequestDTO;
import org.example.greenride.dto.external.JsonPlaceholderPostResponseDTO;

// Service interface για εξωτερικό API demo (JSONPlaceholder)
public interface ExternalPostService {
    // Δημιουργία post στο JSONPlaceholder external API
    JsonPlaceholderPostResponseDTO createPost(JsonPlaceholderPostRequestDTO request);
}
