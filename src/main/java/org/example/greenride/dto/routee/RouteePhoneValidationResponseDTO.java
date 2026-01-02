package org.example.greenride.dto.routee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteePhoneValidationResponseDTO {
    private String phone;
    private Boolean valid;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Boolean getValid() { return valid; }
    public void setValid(Boolean valid) { this.valid = valid; }
}
