package org.example.greenride.service;

import org.example.greenride.dto.user.*;
import org.example.greenride.entity.User;
import org.example.greenride.repository.UserRepository;
import org.example.greenride.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

// Service για authentication (εγγραφή και σύνδεση χρηστών)
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

    // Εγγραφή νέου χρήστη (με validation και JWT token generation)
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
        u.setFullName(dto.getFullName() != null ? dto.getFullName() : dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setRole(dto.getRole());

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

    // Σύνδεση χρήστη (authentication και JWT token generation)
    public AuthResponseDTO login(UserLoginDTO dto) {
        System.out.println("Login attempt for user: " + dto.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User u = userRepository.findByUsername(dto.getUsername());

        // Get the highest role (ADMIN > USER)
        String highestRole = getHighestRole(u);

        String token = jwtService.generateToken(u.getUsername(), highestRole);
        return new AuthResponseDTO(token, u.getId(), u.getUsername(), highestRole);
    }

    // Προσδιορισμός ανώτερου ρόλου (ADMIN > USER)
    private String getHighestRole(User user) {
        // First check if user has ADMIN in their roles collection
        boolean isAdminInRoles = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equalsIgnoreCase(role.getName()));

        if (isAdminInRoles) {
            return "ADMIN";
        }

        // Fall back to the role field
        return user.getRole() != null ? user.getRole() : "USER";
    }

    // Μετατροπή User roles σε authorities
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}