package org.example.greenride.controller;

import org.example.greenride.dto.user.UserDTO;
import org.example.greenride.mapper.UserMapper;
import org.example.greenride.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService; // υποθέτω ότι έχεις ήδη UserService

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.getUserById(id));
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}

