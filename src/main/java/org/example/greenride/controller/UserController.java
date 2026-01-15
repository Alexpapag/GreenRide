package org.example.greenride.controller;

import org.example.greenride.dto.user.UserDTO;
import org.example.greenride.mapper.UserMapper;
import org.example.greenride.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API Controller για διαχείριση χρηστών
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Ανάκτηση ενός χρήστη
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.getUserById(id));
    }

    // Ανάκτηση όλων των χρηστών
    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}

