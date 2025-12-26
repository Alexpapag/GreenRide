package org.example.greenride.service.external;

import org.example.greenride.dto.external.JsonPlaceholderPostRequestDTO;
import org.example.greenride.dto.external.JsonPlaceholderPostResponseDTO;

public interface ExternalPostService {
    JsonPlaceholderPostResponseDTO createPost(JsonPlaceholderPostRequestDTO request);
}
