package org.example.greenride.config;

import org.example.greenride.entity.Role;
import org.example.greenride.entity.User;
import org.example.greenride.repository.RoleRepository;
import org.example.greenride.repository.UserRepository;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.BookingRepository;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.Booking;
import org.example.greenride.repository.ReviewRepository;
import org.example.greenride.entity.Review;
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
                                      RideRepository rideRepository,
                                      BookingRepository bookingRepository,
                                      ReviewRepository reviewRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // ... existing role creation ...
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

            User admin = userRepository.findByUsername("admin");
            // Create admin user if it doesn't exist
            if (admin == null) {
                admin = new User();
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

                admin = userRepository.save(admin);
                System.out.println("Admin user created: admin / admin123");
            }

            User testUser = userRepository.findByUsername("testuser");
            // Create a test regular user if it doesn't exist
            if (testUser == null) {
                testUser = new User();
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

                testUser = userRepository.save(testUser);
                System.out.println("Test user created: testuser / test123");
            }

            // --- ADD DEMO RIDES ---
            if (rideRepository.count() == 0) {
                // 1. COMPLETED Ride (from Berlin to Munich)
                Ride ride1 = new Ride();
                ride1.setDriver(admin);
                ride1.setFromCity("Berlin");
                ride1.setToCity("Munich");
                ride1.setStartDatetime(LocalDateTime.now().minusDays(2));
                ride1.setEndDatetime(LocalDateTime.now().minusDays(2).plusHours(6));
                ride1.setAvailableSeatsTotal(4);
                ride1.setAvailableSeatsRemain(2);
                ride1.setPricePerSeat(new BigDecimal("35.00"));
                ride1.setStatus("COMPLETED");
                ride1.setCreatedAt(LocalDateTime.now().minusDays(5));
                ride1 = rideRepository.save(ride1);

                // Booking for completed ride
                Booking b1 = new Booking();
                b1.setRide(ride1);
                b1.setPassenger(testUser);
                b1.setSeatsBooked(2);
                b1.setTotalPrice(new BigDecimal("70.00"));
                b1.setStatus("COMPLETED");
                b1.setBookedAt(LocalDateTime.now().minusDays(4));
                bookingRepository.save(b1);

                // Add demo reviews
                if (reviewRepository.count() == 0) {
                    Review r1 = new Review();
                    r1.setRide(ride1);
                    r1.setReviewer(testUser);
                    r1.setReviewee(admin);
                    r1.setRating(5);
                    r1.setComment("Great driver, very punctual!");
                    r1.setRoleOfReviewee("DRIVER");
                    r1.setCreatedAt(LocalDateTime.now().minusDays(1));
                    reviewRepository.save(r1);

                    Review r2 = new Review();
                    r2.setRide(ride1);
                    r2.setReviewer(admin);
                    r2.setReviewee(testUser);
                    r2.setRating(4);
                    r2.setComment("Friendly passenger, recommended.");
                    r2.setRoleOfReviewee("PASSENGER");
                    r2.setCreatedAt(LocalDateTime.now().minusDays(1));
                    reviewRepository.save(r2);

                    // Update user averages
                    admin.setRatingAvgDriver(new BigDecimal("5.00"));
                    userRepository.save(admin);
                    testUser.setRatingAvgPassenger(new BigDecimal("4.00"));
                    userRepository.save(testUser);
                }

                // 2. PLANNED Ride (Future)
                Ride ride2 = new Ride();
                ride2.setDriver(testUser);
                ride2.setFromCity("Athens");
                ride2.setToCity("Thessaloniki");
                ride2.setStartDatetime(LocalDateTime.now().plusDays(3));
                ride2.setAvailableSeatsTotal(3);
                ride2.setAvailableSeatsRemain(3);
                ride2.setPricePerSeat(new BigDecimal("25.00"));
                ride2.setStatus("PLANNED");
                ride2.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride2);

                // 3. CANCELLED Ride
                Ride ride3 = new Ride();
                ride3.setDriver(admin);
                ride3.setFromCity("Paris");
                ride3.setToCity("Lyon");
                ride3.setStartDatetime(LocalDateTime.now().minusDays(1));
                ride3.setAvailableSeatsTotal(2);
                ride3.setAvailableSeatsRemain(2);
                ride3.setPricePerSeat(new BigDecimal("20.00"));
                ride3.setStatus("CANCELLED");
                ride3.setCreatedAt(LocalDateTime.now().minusDays(3));
                rideRepository.save(ride3);

                System.out.println("Demo rides and bookings created!");
            }
        };
    }
}