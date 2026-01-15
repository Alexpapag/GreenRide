package org.example.greenride.repository;

import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository για διαχείριση UserReport entities (αναφορές χρηστών)
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    // Αναζήτηση αναφορών για συγκεκριμένο χρήστη που αναφέρθηκε
    List<UserReport> findByReportedUser(User reportedUser);

    // Μέτρηση αναφορών με βάση status (PENDING/RESOLVED)
    long countByStatus(String status);

    // Αναζήτηση αναφορών με βάση status
    List<UserReport> findByStatus(String status);
}
