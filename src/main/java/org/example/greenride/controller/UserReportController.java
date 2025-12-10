package org.example.greenride.controller;
import org.example.greenride.entity.UserReport;
import org.example.greenride.service.UserReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class UserReportController {

    private final UserReportService reportService;

    public UserReportController(UserReportService reportService) {
        this.reportService = reportService;
    }

    // CREATE REPORT
    @PostMapping
    public ResponseEntity<UserReport> createReport(@RequestBody Map<String, Object> body) {

        Long reportedUserId = Long.valueOf(body.get("reportedUserId").toString());
        Long reporterUserId = body.get("reporterUserId") == null ? null :
                Long.valueOf(body.get("reporterUserId").toString());

        String reason = body.get("reason").toString();

        UserReport created = reportService.createReport(reportedUserId, reporterUserId, reason);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL
    @GetMapping
    public List<UserReport> getAllReports() {
        return reportService.getAllReports();
    }

    // READ ONE
    @GetMapping("/{id}")
    public UserReport getReport(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public UserReport updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return reportService.updateStatus(id, body.get("status"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}


