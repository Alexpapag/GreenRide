package org.example.greenride.dto.routee;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteeSmsAnalyzeResponseDTO {

    @JsonProperty("message_parts")
    private Integer messageParts;

    @JsonProperty("characters")
    private Integer characters;

    // υπάρχουν κι άλλα fields στο Routee response, κρατάμε minimal
    public RouteeSmsAnalyzeResponseDTO() {}

    public Integer getMessageParts() { return messageParts; }
    public void setMessageParts(Integer messageParts) { this.messageParts = messageParts; }

    public Integer getCharacters() { return characters; }
    public void setCharacters(Integer characters) { this.characters = characters; }
}
