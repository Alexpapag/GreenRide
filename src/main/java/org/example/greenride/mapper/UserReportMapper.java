package org.example.greenride.mapper;

import org.example.greenride.dto.userreport.UserReportRequestDTO;
import org.example.greenride.dto.userreport.UserReportResponseDTO;
import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;

public final class UserReportMapper {
    private UserReportMapper() {}

    public static UserReportResponseDTO toResponseDTO(UserReport r) {
        if (r == null) return null;

        UserReportResponseDTO dto = new UserReportResponseDTO();
        dto.setId(r.getId());

        if (r.getReportedUser() != null) dto.setReportedUserId(r.getReportedUser().getId());
        if (r.getReporterUser() != null) dto.setReporterUserId(r.getReporterUser().getId());

        dto.setReason(r.getReason());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setResolvedAt(r.getResolvedAt());

        return dto;
    }

    public static UserReport fromRequestDTO(UserReportRequestDTO dto, User reported, User reporterOrNull) {
        UserReport r = new UserReport();
        r.setReportedUser(reported);
        r.setReporterUser(reporterOrNull);
        r.setReason(dto.getReason());
        return r;
    }
}
