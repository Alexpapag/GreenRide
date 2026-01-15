package org.example.greenride.service.routee;

// Service interface για Routee API authentication (OAuth2 token management)
public interface RouteeAuthService {
    // Ανάκτηση access token για Routee API (με caching)
    String getAccessToken();
}
