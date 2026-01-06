package org.example.greenride.repository;
import org.example.greenride.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // extra queries αν θες
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsername(String username);

    // New methods for AdminService
    long countByStatus(String status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    List<User> findTop5ByOrderByCreatedAtDesc();

    List<User> findByUsernameContainingOrEmailContainingOrFullNameContaining(
            String username, String email, String fullName);


}
