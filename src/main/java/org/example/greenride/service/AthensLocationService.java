package org.example.greenride.service;

import org.example.greenride.dto.geo.AthensLocation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AthensLocationService {

    private final Map<String, AthensLocation> locations = new HashMap<>();

    public AthensLocationService() {
        // Popular Athens locations with pre-calculated coordinates
        addLocation("Syntagma Square", "Syntagma Square, Athens", 37.9755, 23.7348);
        addLocation("Acropolis", "Acropolis, Athens", 37.9715, 23.7257);
        addLocation("Monastiraki", "Monastiraki Square, Athens", 37.9762, 23.7255);
        addLocation("Plaka", "Plaka, Athens", 37.9731, 23.7275);
        addLocation("Kolonaki", "Kolonaki, Athens", 37.9794, 23.7416);
        addLocation("Glyfada", "Glyfada, Athens", 37.8651, 23.7539);
        addLocation("Piraeus", "Piraeus Port, Athens", 37.9421, 23.6464);
        addLocation("Athens Airport", "Athens International Airport", 37.9364, 23.9445);
        addLocation("Kifisia", "Kifisia, Athens", 38.0746, 23.8114);
        addLocation("Marousi", "Marousi, Athens", 38.0503, 23.8087);
        addLocation("Chalandri", "Chalandri, Athens", 38.0217, 23.7972);
        addLocation("Nea Smyrni", "Nea Smyrni, Athens", 37.9493, 23.7171);
        addLocation("Kallithea", "Kallithea, Athens", 37.9537, 23.7019);
        addLocation("Peristeri", "Peristeri, Athens", 38.0117, 23.6917);
        addLocation("Agia Paraskevi", "Agia Paraskevi, Athens", 38.0167, 23.8194);
        addLocation("Voula", "Voula, Athens", 37.8431, 23.7667);
        addLocation("Vouliagmeni", "Vouliagmeni, Athens", 37.8167, 23.7667);
        addLocation("Psychiko", "Psychiko, Athens", 38.0167, 23.7750);
        addLocation("Pagrati", "Pagrati, Athens", 37.9667, 23.7500);
        addLocation("Exarchia", "Exarchia, Athens", 37.9889, 23.7333);
        addLocation("Omonia Square", "Omonia Square, Athens", 37.9842, 23.7275);
        addLocation("Victoria Square", "Victoria Square, Athens", 37.9944, 23.7278);
        addLocation("Patisia", "Patisia, Athens", 38.0083, 23.7333);
        addLocation("Zografou", "Zografou, Athens", 37.9833, 23.7667);
        addLocation("Ilisia", "Ilisia, Athens", 37.9833, 23.7500);
    }

    private void addLocation(String name, String displayName, double lat, double lon) {
        locations.put(name.toLowerCase(), new AthensLocation(name, displayName, lat, lon));
    }

    public List<AthensLocation> getAllLocations() {
        return new ArrayList<>(locations.values());
    }

    public List<AthensLocation> searchLocations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllLocations();
        }

        String lowerQuery = query.toLowerCase();
        return locations.values().stream()
                .filter(loc -> loc.getName().toLowerCase().contains(lowerQuery) ||
                              loc.getDisplayName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public Optional<AthensLocation> getLocation(String name) {
        return Optional.ofNullable(locations.get(name.toLowerCase()));
    }

    public boolean hasLocation(String name) {
        return locations.containsKey(name.toLowerCase());
    }
}
