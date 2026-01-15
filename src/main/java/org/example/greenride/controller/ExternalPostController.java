package org.example.greenride.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.greenride.dto.external.JsonPlaceholderPostRequestDTO;
import org.example.greenride.dto.external.JsonPlaceholderPostResponseDTO;
import org.example.greenride.service.external.ExternalPostService;
import org.springframework.web.bind.annotation.*;

// REST API Controller για demo κλήση εξωτερικού API (JSONPlaceholder)
@RestController
@RequestMapping("/external")
@Tag(name = "External APIs", description = "Endpoints που καλούν εξωτερικές υπηρεσίες")
public class ExternalPostController {

    private final ExternalPostService externalPostService;

    public ExternalPostController(ExternalPostService externalPostService) {
        this.externalPostService = externalPostService;
    }

    // Demo POST σε εξωτερικό API
    @PostMapping("/jsonplaceholder/posts")
    @Operation(summary = "POST to external API (JSONPlaceholder)", description = "Κάνει POST σε εξωτερική υπηρεσία και επιστρέφει την απάντηση.")
    public JsonPlaceholderPostResponseDTO createExternalPost(@Valid @RequestBody JsonPlaceholderPostRequestDTO request) {
        return externalPostService.createPost(request);
    }
}
