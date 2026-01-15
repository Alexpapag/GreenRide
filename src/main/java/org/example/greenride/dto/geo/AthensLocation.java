package org.example.greenride.dto.geo;
// DTO για προκαθορισμένες τοποθεσίες Αθήνας

public class AthensLocation {
    private String name;
    private String displayName;
    private double lat;
    private double lon;

    public AthensLocation() {
    }

    public AthensLocation(String name, String displayName, double lat, double lon) {
        this.name = name;
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
