package org.example.greenride.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

// Handler για επιτυχημένο authentication - redirect με βάση role
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    // Μετά από επιτυχημένη σύνδεση, redirect ανάλογα με τον ρόλο
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Έλεγχος αν ο χρήστης είναι ADMIN
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Redirect με βάση τον ρόλο
        if (isAdmin) {
            response.sendRedirect("/admin/dashboard"); // Admin -> admin dashboard
        } else {
            response.sendRedirect("/web/dashboard"); // User -> user dashboard
        }
    }
}