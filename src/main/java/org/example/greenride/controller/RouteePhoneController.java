package org.example.greenride.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.greenride.dto.routee.RouteePhoneValidationResponseDTO;
import org.example.greenride.service.routee.RouteePhoneValidationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external/routee")
@Tag(name = "External APIs (Routee)", description = "Secured Routee calls (OAuth token + Bearer)")
public class RouteePhoneController {

    private final RouteePhoneValidationService service;

    public RouteePhoneController(RouteePhoneValidationService service) {
        this.service = service;
    }

    @GetMapping("/phone/validate")
    @Operation(summary = "Routee phone validation (secured)", description = "Gets OAuth token then calls Routee phone validation with Bearer token.")
    public RouteePhoneValidationResponseDTO validate(@RequestParam("phone") String phoneE164) {
        return service.validate(phoneE164);
    }
}
