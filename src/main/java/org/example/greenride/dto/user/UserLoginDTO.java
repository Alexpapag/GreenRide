package org.example.greenride.dto.user;

import jakarta.validation.constraints.NotBlank;
// DTO για αίτημα σύνδεσης χρήστη

public class UserLoginDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
