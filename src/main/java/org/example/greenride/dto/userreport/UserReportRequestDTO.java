package org.example.greenride.dto.userreport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
// DTO για αίτημα αναφοράς χρήστη

public class UserReportRequestDTO {

    @NotNull
    private Long reportedUserId;

    private Long reporterUserId; // nullable 

    @NotBlank
    @Size(max = 1000)
    private String reason;

    public Long getReportedUserId() { return reportedUserId; }
    public void setReportedUserId(Long reportedUserId) { this.reportedUserId = reportedUserId; }

    public Long getReporterUserId() { return reporterUserId; }
    public void setReporterUserId(Long reporterUserId) { this.reporterUserId = reporterUserId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
