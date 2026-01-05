package org.example.greenride.repository;

import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByReportedUser(User reportedUser);

    // New methods for AdminService
    long countByStatus(String status);

    List<UserReport> findByStatus(String status);
}
