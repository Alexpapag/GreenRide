package org.example.greenride.dto.routee;

import jakarta.validation.constraints.NotBlank;

public class RouteeSmsAnalyzeRequestDTO {

    @NotBlank(message = "body is required")
    private String body;

    // routee συνήθως θέλει E.164 ή διεθνές format
    @NotBlank(message = "to is required")
    private String to;

    @NotBlank(message = "from is required")
    private String from;

    public RouteeSmsAnalyzeRequestDTO() {}

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
}
