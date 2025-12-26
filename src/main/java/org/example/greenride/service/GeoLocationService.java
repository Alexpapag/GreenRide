package org.example.greenride.service;

import org.example.greenride.dto.geo.GeoLocationResponseDTO;

public interface GeoLocationService {
    GeoLocationResponseDTO forwardGeocode(String query);
}
