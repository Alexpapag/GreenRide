package org.example.greenride.dto.external;
// DTO για απόκριση από εξωτερικό API

public class JsonPlaceholderPostResponseDTO {

    private Integer id;
    private String title;
    private String body;
    private Integer userId;

    public JsonPlaceholderPostResponseDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
