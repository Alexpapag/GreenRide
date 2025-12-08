package org.example.greenride.service;

import org.example.greenride.entity.User;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
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

    // READ (όλοι)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ (ένας)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // UPDATE
    public User updateUser(Long id, User updated) {
        User existing = getUserById(id);
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setStatus(updated.getStatus());
        existing.setRole(updated.getRole());
        return userRepository.save(existing);
    }

    // DELETE
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
}
