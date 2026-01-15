package org.example.greenride.dto.external;
// DTO για demo POST σε εξωτερικό API

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JsonPlaceholderPostRequestDTO {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "body is required")
    private String body;

    @NotNull(message = "userId is required")
    private Integer userId;

    public JsonPlaceholderPostRequestDTO() {}

    public JsonPlaceholderPostRequestDTO(String title, String body, Integer userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
