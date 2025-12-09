package org.example.greenride.service;

import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;
import org.example.greenride.repository.UserReportRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    public UserReportService(UserReportRepository userReportRepository,
                             UserRepository userRepository) {
        this.userReportRepository = userReportRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public UserReport createReport(Long reportedUserId,
                                   Long reporterUserId,   // μπορεί να είναι null
                                   String reason) {

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }

        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        User reporterUser = null;
        if (reporterUserId != null) {
            reporterUser = userRepository.findById(reporterUserId)
                    .orElseThrow(() -> new IllegalArgumentException("Reporter user not found"));
        }

        UserReport report = new UserReport();
        report.setReportedUser(reportedUser);
        report.setReporterUser(reporterUser);
        report.setReason(reason);
        report.setStatus("OPEN");
        report.setCreatedAt(LocalDateTime.now());
        report.setResolvedAt(null);

        return userReportRepository.save(report);
    }

    // READ ALL
    public List<UserReport> getAllReports() {
        return userReportRepository.findAll();
    }

    // READ ONE
    public UserReport getReportById(Long id) {
        return userReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }

    // READ reports για συγκεκριμένο χρήστη (που έχει γίνει report)
    public List<UserReport> getReportsForUser(Long reportedUserId) {
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userReportRepository.findByReportedUser(reportedUser);
    }

    // Αλλαγή status (π.χ. OPEN -> REVIEWED -> CLOSED)
    public UserReport updateStatus(Long reportId, String newStatus) {
        UserReport report = getReportById(reportId);

        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        report.setStatus(newStatus);

        if ("CLOSED".equalsIgnoreCase(newStatus)) {
            report.setResolvedAt(LocalDateTime.now());
        }

        return userReportRepository.save(report);
    }

    // UPDATE reason (αν χρειάζεται)
    public UserReport updateReason(Long reportId, String reason) {
        UserReport report = getReportById(reportId);

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }

        report.setReason(reason);
        return userReportRepository.save(report);
    }

    // DELETE
    public void deleteReport(Long id) {
        if (!userReportRepository.existsById(id)) {
            throw new IllegalArgumentException("Report not found");
        }
        userReportRepository.deleteById(id);
    }
}
