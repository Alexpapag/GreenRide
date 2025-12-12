package org.example.greenride.controller;

import jakarta.validation.Valid;
import org.example.greenride.dto.userreport.UserReportRequestDTO;
import org.example.greenride.dto.userreport.UserReportResponseDTO;
import org.example.greenride.entity.UserReport;
import org.example.greenride.mapper.UserReportMapper;
import org.example.greenride.service.UserReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-reports")
public class UserReportController {

    private final UserReportService userReportService;

    public UserReportController(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReportResponseDTO create(@Valid @RequestBody UserReportRequestDTO dto) {
        UserReport created = userReportService.createReport(dto);
        return UserReportMapper.toResponseDTO(created);
    }

    @GetMapping("/{id}")
    public UserReportResponseDTO getById(@PathVariable Long id) {
        return UserReportMapper.toResponseDTO(userReportService.getById(id));
    }

    @GetMapping
    public List<UserReportResponseDTO> getAll() {
        return userReportService.getAll()
                .stream()
                .map(UserReportMapper::toResponseDTO)
                .toList();
    }
}



