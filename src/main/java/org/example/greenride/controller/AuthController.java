package org.example.greenride.controller;

import jakarta.validation.Valid;
import org.example.greenride.dto.user.*;
import org.example.greenride.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Controller για authentication (εγγραφή και σύνδεση χρηστών)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Εγγραφή νέου χρήστη
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@Valid @RequestBody UserRegistrationDTO dto) {
        return authService.register(dto);
    }

    // Σύνδεση χρήστη και επιστροφή JWT token
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody UserLoginDTO dto) {
        return authService.login(dto);
    }
}
