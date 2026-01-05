package org.example.greenride.config;

import org.example.greenride.entity.Role;
import org.example.greenride.entity.User;
import org.example.greenride.repository.RoleRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      RoleRepository roleRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role roleUser = roleRepository.findByName("ROLE_USER");
            if (roleUser == null) {
                roleUser = new Role("ROLE_USER");
                roleRepository.save(roleUser);
            }

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (roleAdmin == null) {
                roleAdmin = new Role("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }

            // Create admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // password: admin123
                admin.setFullName("System Administrator");
                admin.setEmail("admin@greenride.com");
                admin.setPhone("1234567890");
                admin.setStatus("ACTIVE");
                admin.setRole("ADMIN");
                admin.setRatingAvgDriver(BigDecimal.ZERO);
                admin.setRatingAvgPassenger(BigDecimal.ZERO);
                admin.setCreatedAt(LocalDateTime.now());

                // Add ADMIN role to the roles set
                Set<Role> roles = new HashSet<>();
                roles.add(roleAdmin);
                admin.setRoles(roles);

                userRepository.save(admin);
                System.out.println("Admin user created: admin / admin123");
            }

            // Create a test regular user if it doesn't exist
            if (!userRepository.existsByUsername("testuser")) {
                User testUser = new User();
                testUser.setUsername("testuser");
                testUser.setPassword(passwordEncoder.encode("test123")); // password: test123
                testUser.setFullName("Test User");
                testUser.setEmail("test@greenride.com");
                testUser.setPhone("0987654321");
                testUser.setStatus("ACTIVE");
                testUser.setRole("USER");
                testUser.setRatingAvgDriver(BigDecimal.ZERO);
                testUser.setRatingAvgPassenger(BigDecimal.ZERO);
                testUser.setCreatedAt(LocalDateTime.now());

                // Add USER role
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                testUser.setRoles(roles);

                userRepository.save(testUser);
                System.out.println("Test user created: testuser / test123");
            }
        };
    }
}