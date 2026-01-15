package org.example.greenride.repository;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Repository για διαχείριση User entities (JPA)
public interface UserRepository extends JpaRepository<User, Long> {

    // Έλεγχος ύπαρξης username
    boolean existsByUsername(String username);
    // Έλεγχος ύπαρξης email
    boolean existsByEmail(String email);
    // Αναζήτηση χρήστη με βάση username
    User findByUsername(String username);

    // Μέτρηση χρηστών με βάση status (ACTIVE/BANNED/SUSPENDED)
    long countByStatus(String status);

    // Μέτρηση χρηστών που δημιουργήθηκαν μετά από συγκεκριμένη ημερομηνία
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    // Αναζήτηση 5 πιο πρόσφατων χρηστών
    List<User> findTop5ByOrderByCreatedAtDesc();

    // Αναζήτηση χρηστών με βάση username, email ή fullName (για search)
    List<User> findByUsernameContainingOrEmailContainingOrFullNameContaining(
            String username, String email, String fullName);


}
