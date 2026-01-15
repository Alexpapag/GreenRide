package org.example.greenride.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Κεντρική ρύθμιση Spring Security (JWT authentication, role-based access)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Ρύθμιση Security Filter Chain (authorization rules, JWT filter)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Απενεργοποίηση CSRF (για stateless JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions (JWT)
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (χωρίς authentication)
                        .requestMatchers(
                                "/", "/web/**", "/css/**", "/auth/**", "/geo/**",
                                "/external/**", "/routee/**", "/h2-console/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/debug/**", "/error"
                        ).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "*.ico").permitAll()

                        // Admin endpoints (προσωρινά χωρίς έλεγχο για testing)
                        .requestMatchers("/admin/**").permitAll() // Changed from hasRole("ADMIN")

                        // Όλα τα υπόλοιπα endpoints απαιτούν authentication
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Για H2 console
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT filter πριν το standard auth

        return http.build();
    }

    // Password encoder (BCrypt για hashing passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Manager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}