package org.example.greenride.mapper;

import org.example.greenride.dto.user.UserDTO;
import org.example.greenride.entity.User;

// Mapper για μετατροπή User entity σε UserDTO
public final class UserMapper {
    private UserMapper() {}

    // Μετατροπή από User entity σε UserDTO
    public static UserDTO toDTO(User u) {
        if (u == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setStatus(u.getStatus());
        dto.setRole(u.getRole());

        dto.setRatingAvgDriver(u.getRatingAvgDriver());
        dto.setRatingAvgPassenger(u.getRatingAvgPassenger());

        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }
}
