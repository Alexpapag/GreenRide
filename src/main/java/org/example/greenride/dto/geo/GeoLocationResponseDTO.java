package org.example.greenride.dto.geo;

public class GeoLocationResponseDTO {
    private Double lat;
    private Double lon;
    private String displayName;

    public GeoLocationResponseDTO() {}

    public GeoLocationResponseDTO(Double lat, Double lon, String displayName) {
        this.lat = lat;
        this.lon = lon;
        this.displayName = displayName;
    }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
