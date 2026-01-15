package org.example.greenride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    // reported_user_id -> User.user_id (ο χρήστης που γίνεται report)
    @ManyToOne(optional = false)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser;

    // reporter_user_id -> User.user_id (ο χρήστης που κάνει το report, nullable στο ER)
    @ManyToOne
    @JoinColumn(name = "reporter_user_id")
    private User reporterUser;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    // OPEN, REVIEWED, CLOSED
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    // Constructors 

    public UserReport() {
    }

    public UserReport(User reportedUser,
                      User reporterUser,
                      String reason,
                      String status,
                      LocalDateTime createdAt,
                      LocalDateTime resolvedAt) {
        this.reportedUser = reportedUser;
        this.reporterUser = reporterUser;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    // Getters & Setters 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public User getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(User reporterUser) {
        this.reporterUser = reporterUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
