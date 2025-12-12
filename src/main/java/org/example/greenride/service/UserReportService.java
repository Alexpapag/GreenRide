package org.example.greenride.service;

import org.example.greenride.dto.userreport.UserReportRequestDTO;
import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;
import org.example.greenride.mapper.UserReportMapper;
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

    public UserReport createReport(UserReportRequestDTO dto) {
        User reported = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        User reporter = null;
        if (dto.getReporterUserId() != null) {
            reporter = userRepository.findById(dto.getReporterUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Reporter user not found"));
        }

        UserReport report = UserReportMapper.fromRequestDTO(dto, reported, reporter);
        report.setStatus("OPEN");
        report.setCreatedAt(LocalDateTime.now());

        return userReportRepository.save(report);
    }

    public UserReport getById(Long id) {
        return userReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }

    public List<UserReport> getAll() {
        return userReportRepository.findAll();
    }
}

