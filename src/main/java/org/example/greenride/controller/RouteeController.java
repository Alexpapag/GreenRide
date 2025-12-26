package org.example.greenride.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.greenride.dto.routee.RouteeSmsAnalyzeRequestDTO;
import org.example.greenride.dto.routee.RouteeSmsAnalyzeResponseDTO;
import org.example.greenride.service.routee.RouteeMessagingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external/routee")
@Tag(name = "External APIs (Routee)", description = "Secured external API calls with OAuth2 token")
public class RouteeController {

    private final RouteeMessagingService routeeMessagingService;

    public RouteeController(RouteeMessagingService routeeMessagingService) {
        this.routeeMessagingService = routeeMessagingService;
    }

    @PostMapping("/sms/analyze")
    @Operation(summary = "Analyze SMS (secured Routee call)", description = "Παίρνει OAuth token από Routee και κάνει secured call με Bearer token.")
    public RouteeSmsAnalyzeResponseDTO analyze(@Valid @RequestBody RouteeSmsAnalyzeRequestDTO request) {
        return routeeMessagingService.analyzeSms(request);
    }
}
