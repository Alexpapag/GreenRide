package org.example.greenride.service;

import jakarta.servlet.http.HttpSession;
import org.example.greenride.entity.User;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

// Service για διαχείριση χρηστών (CRUD operations)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Δημιουργία νέου χρήστη
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    // Ανάκτηση όλων των χρηστών
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Ανάκτηση χρήστη με βάση ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // Ενημέρωση χρήστη
    public User updateUser(Long id, User updated) {
        User existing = getUserById(id);
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setStatus(updated.getStatus());
        existing.setRole(updated.getRole());
        return userRepository.save(existing);
    }

    // Διαγραφή χρήστη
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    // Ανάκτηση τρέχοντος χρήστη από HTTP request session
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("No active session found");
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        return getUserById(userId);
    }

    // Ανάκτηση τρέχοντος χρήστη από HttpSession
    public User getCurrentUserFromSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }
        return getUserById(userId);
    }
}
