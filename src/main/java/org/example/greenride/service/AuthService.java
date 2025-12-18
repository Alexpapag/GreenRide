package org.example.greenride.service;

import org.example.greenride.dto.user.*;
import org.example.greenride.entity.User;
import org.example.greenride.repository.UserRepository;
import org.example.greenride.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO register(UserRegistrationDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());

        // defaults
        u.setStatus("ACTIVE");
        if (u.getRole() == null) u.setRole("USER");

        if (u.getRatingAvgDriver() == null) u.setRatingAvgDriver(BigDecimal.ZERO);
        if (u.getRatingAvgPassenger() == null) u.setRatingAvgPassenger(BigDecimal.ZERO);

        if (u.getCreatedAt() == null) u.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(u);

        String token = jwtService.generateToken(saved.getUsername(), saved.getRole());
        return new AuthResponseDTO(token, saved.getId(), saved.getUsername(), saved.getRole());
    }

    public AuthResponseDTO login(UserLoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User u = userRepository.findByUsername(dto.getUsername());
        String token = jwtService.generateToken(u.getUsername(), u.getRole());
        return new AuthResponseDTO(token, u.getId(), u.getUsername(), u.getRole());
    }
}
