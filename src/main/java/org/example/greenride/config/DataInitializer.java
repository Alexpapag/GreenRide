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

            // Create 5 test users for Greek universities
            User maria = userRepository.findByUsername("maria");
            if (maria == null) {
                maria = new User();
                maria.setUsername("maria");
                maria.setPassword(passwordEncoder.encode("maria123"));
                maria.setFullName("Μαρία Παπαδοπούλου");
                maria.setEmail("maria@greenride.gr");
                maria.setPhone("6971234567");
                maria.setStatus("ACTIVE");
                maria.setRole("USER");
                maria.setRatingAvgDriver(new BigDecimal("4.8"));
                maria.setRatingAvgPassenger(new BigDecimal("4.9"));
                maria.setCreatedAt(LocalDateTime.now());
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                maria.setRoles(roles);
                maria = userRepository.save(maria);
                System.out.println("User created: maria / maria123");
            }

            User nikos = userRepository.findByUsername("nikos");
            if (nikos == null) {
                nikos = new User();
                nikos.setUsername("nikos");
                nikos.setPassword(passwordEncoder.encode("nikos123"));
                nikos.setFullName("Νίκος Γεωργίου");
                nikos.setEmail("nikos@greenride.gr");
                nikos.setPhone("6972345678");
                nikos.setStatus("ACTIVE");
                nikos.setRole("USER");
                nikos.setRatingAvgDriver(new BigDecimal("4.7"));
                nikos.setRatingAvgPassenger(new BigDecimal("4.6"));
                nikos.setCreatedAt(LocalDateTime.now());
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                nikos.setRoles(roles);
                nikos = userRepository.save(nikos);
                System.out.println("User created: nikos / nikos123");
            }

            User elena = userRepository.findByUsername("elena");
            if (elena == null) {
                elena = new User();
                elena.setUsername("elena");
                elena.setPassword(passwordEncoder.encode("elena123"));
                elena.setFullName("Ελένη Κωνσταντίνου");
                elena.setEmail("elena@greenride.gr");
                elena.setPhone("6973456789");
                elena.setStatus("ACTIVE");
                elena.setRole("USER");
                elena.setRatingAvgDriver(new BigDecimal("4.9"));
                elena.setRatingAvgPassenger(new BigDecimal("5.0"));
                elena.setCreatedAt(LocalDateTime.now());
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                elena.setRoles(roles);
                elena = userRepository.save(elena);
                System.out.println("User created: elena / elena123");
            }

            User dimitris = userRepository.findByUsername("dimitris");
            if (dimitris == null) {
                dimitris = new User();
                dimitris.setUsername("dimitris");
                dimitris.setPassword(passwordEncoder.encode("dimitris123"));
                dimitris.setFullName("Δημήτρης Νικολάου");
                dimitris.setEmail("dimitris@greenride.gr");
                dimitris.setPhone("6974567890");
                dimitris.setStatus("ACTIVE");
                dimitris.setRole("USER");
                dimitris.setRatingAvgDriver(new BigDecimal("4.5"));
                dimitris.setRatingAvgPassenger(new BigDecimal("4.7"));
                dimitris.setCreatedAt(LocalDateTime.now());
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                dimitris.setRoles(roles);
                dimitris = userRepository.save(dimitris);
                System.out.println("User created: dimitris / dimitris123");
            }

            User sofia = userRepository.findByUsername("sofia");
            if (sofia == null) {
                sofia = new User();
                sofia.setUsername("sofia");
                sofia.setPassword(passwordEncoder.encode("sofia123"));
                sofia.setFullName("Σοφία Αθανασίου");
                sofia.setEmail("sofia@greenride.gr");
                sofia.setPhone("6975678901");
                sofia.setStatus("ACTIVE");
                sofia.setRole("USER");
                sofia.setRatingAvgDriver(new BigDecimal("4.6"));
                sofia.setRatingAvgPassenger(new BigDecimal("4.8"));
                sofia.setCreatedAt(LocalDateTime.now());
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                sofia.setRoles(roles);
                sofia = userRepository.save(sofia);
                System.out.println("User created: sofia / sofia123");
            }

            //  DEMO RIDES WITH GREEK UNIVERSITY LOCATIONS 
            if (rideRepository.count() == 0) {
                // Ride 1: ΕΚΠΑ Ιλίσια -> ΕΜΠ Ζωγράφου
                Ride ride1 = new Ride();
                ride1.setDriver(maria);
                ride1.setFromCity("ΕΚΠΑ Ιλίσια");
                ride1.setFromAddress("Πανεπιστημιούπολη Ιλισίων, Αθήνα");
                ride1.setToCity("ΕΜΠ Ζωγράφου");
                ride1.setToAddress("Πολυτεχνειούπολη Ζωγράφου, Αθήνα");
                ride1.setStartDatetime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
                ride1.setAvailableSeatsTotal(3);
                ride1.setAvailableSeatsRemain(3);
                ride1.setPricePerSeat(new BigDecimal("3.50"));
                ride1.setStatus("PLANNED");
                ride1.setDistanceKm(new BigDecimal("5.2"));
                ride1.setEstimatedDurationMin(15);
                ride1.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride1);

                // Ride 2: Οικονομικό Πανεπιστήμιο -> Πάντειο
                Ride ride2 = new Ride();
                ride2.setDriver(nikos);
                ride2.setFromCity("Οικονομικό Πανεπιστήμιο Αθηνών");
                ride2.setFromAddress("Πατησίων 76, Αθήνα");
                ride2.setToCity("Πάντειο Πανεπιστήμιο");
                ride2.setToAddress("Λεωφ. Συγγρού 136, Καλλιθέα");
                ride2.setStartDatetime(LocalDateTime.now().plusDays(2).withHour(10).withMinute(30));
                ride2.setAvailableSeatsTotal(4);
                ride2.setAvailableSeatsRemain(2);
                ride2.setPricePerSeat(new BigDecimal("4.00"));
                ride2.setStatus("PLANNED");
                ride2.setDistanceKm(new BigDecimal("7.8"));
                ride2.setEstimatedDurationMin(20);
                ride2.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride2);

                // Ride 3: ΑΠΘ Θεσσαλονίκη -> ΕΚΠΑ Ιλίσια (μακρινή διαδρομή)
                Ride ride3 = new Ride();
                ride3.setDriver(elena);
                ride3.setFromCity("Θεσσαλονίκη");
                ride3.setFromAddress("ΑΠΘ, Πανεπιστημιούπολη");
                ride3.setToCity("ΕΚΠΑ Ιλίσια");
                ride3.setToAddress("Πανεπιστημιούπολη Ιλισίων, Αθήνα");
                ride3.setStartDatetime(LocalDateTime.now().plusDays(3).withHour(7).withMinute(0));
                ride3.setAvailableSeatsTotal(3);
                ride3.setAvailableSeatsRemain(1);
                ride3.setPricePerSeat(new BigDecimal("35.00"));
                ride3.setStatus("PLANNED");
                ride3.setDistanceKm(new BigDecimal("502"));
                ride3.setEstimatedDurationMin(320);
                ride3.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride3);

                // Ride 4: ΑΣΟΕΕ -> ΕΜΠ
                Ride ride4 = new Ride();
                ride4.setDriver(dimitris);
                ride4.setFromCity("ΑΣΟΕΕ");
                ride4.setFromAddress("Πατησίων 76, Αθήνα");
                ride4.setToCity("ΕΜΠ Ζωγράφου");
                ride4.setToAddress("Πολυτεχνειούπολη Ζωγράφου");
                ride4.setStartDatetime(LocalDateTime.now().plusHours(5).withMinute(0));
                ride4.setAvailableSeatsTotal(2);
                ride4.setAvailableSeatsRemain(2);
                ride4.setPricePerSeat(new BigDecimal("3.00"));
                ride4.setStatus("PLANNED");
                ride4.setDistanceKm(new BigDecimal("6.5"));
                ride4.setEstimatedDurationMin(18);
                ride4.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride4);

                // Ride 5: Παν. Πειραιώς -> ΕΚΠΑ Ιλίσια
                Ride ride5 = new Ride();
                ride5.setDriver(sofia);
                ride5.setFromCity("Πανεπιστήμιο Πειραιώς");
                ride5.setFromAddress("Καραολή και Δημητρίου 80, Πειραιάς");
                ride5.setToCity("ΕΚΠΑ Ιλίσια");
                ride5.setToAddress("Πανεπιστημιούπολη Ιλισίων, Αθήνα");
                ride5.setStartDatetime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
                ride5.setAvailableSeatsTotal(3);
                ride5.setAvailableSeatsRemain(3);
                ride5.setPricePerSeat(new BigDecimal("4.50"));
                ride5.setStatus("PLANNED");
                ride5.setDistanceKm(new BigDecimal("12.5"));
                ride5.setEstimatedDurationMin(25);
                ride5.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride5);

                // Ride 6: ΕΜΠ -> Πάντειο
                Ride ride6 = new Ride();
                ride6.setDriver(maria);
                ride6.setFromCity("ΕΜΠ Ζωγράφου");
                ride6.setFromAddress("Πολυτεχνειούπολη Ζωγράφου");
                ride6.setToCity("Πάντειο Πανεπιστήμιο");
                ride6.setToAddress("Λεωφ. Συγγρού 136, Καλλιθέα");
                ride6.setStartDatetime(LocalDateTime.now().plusDays(2).withHour(16).withMinute(30));
                ride6.setAvailableSeatsTotal(4);
                ride6.setAvailableSeatsRemain(4);
                ride6.setPricePerSeat(new BigDecimal("5.00"));
                ride6.setStatus("PLANNED");
                ride6.setDistanceKm(new BigDecimal("8.3"));
                ride6.setEstimatedDurationMin(22);
                ride6.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride6);

                // Ride 7: Χαροκόπειο -> ΕΚΠΑ Ιλίσια
                Ride ride7 = new Ride();
                ride7.setDriver(nikos);
                ride7.setFromCity("Χαροκόπειο Πανεπιστήμιο");
                ride7.setFromAddress("Ελ. Βενιζέλου 70, Καλλιθέα");
                ride7.setToCity("ΕΚΠΑ Ιλίσια");
                ride7.setToAddress("Πανεπιστημιούπολη Ιλισίων, Αθήνα");
                ride7.setStartDatetime(LocalDateTime.now().plusDays(3).withHour(11).withMinute(0));
                ride7.setAvailableSeatsTotal(3);
                ride7.setAvailableSeatsRemain(1);
                ride7.setPricePerSeat(new BigDecimal("3.80"));
                ride7.setStatus("PLANNED");
                ride7.setDistanceKm(new BigDecimal("6.8"));
                ride7.setEstimatedDurationMin(19);
                ride7.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride7);

                // Ride 8: ΕΚΠΑ Ιλίσια -> Παν. Πειραιώς
                Ride ride8 = new Ride();
                ride8.setDriver(elena);
                ride8.setFromCity("ΕΚΠΑ Ιλίσια");
                ride8.setFromAddress("Πανεπιστημιούπολη Ιλισίων, Αθήνα");
                ride8.setToCity("Πανεπιστήμιο Πειραιώς");
                ride8.setToAddress("Καραολή και Δημητρίου 80, Πειραιάς");
                ride8.setStartDatetime(LocalDateTime.now().plusDays(4).withHour(8).withMinute(30));
                ride8.setAvailableSeatsTotal(2);
                ride8.setAvailableSeatsRemain(2);
                ride8.setPricePerSeat(new BigDecimal("4.50"));
                ride8.setStatus("PLANNED");
                ride8.setDistanceKm(new BigDecimal("12.5"));
                ride8.setEstimatedDurationMin(25);
                ride8.setCreatedAt(LocalDateTime.now());
                rideRepository.save(ride8);

                // Ride 9: ΕΜΠ -> ΑΣΟΕΕ (COMPLETED - past ride)
                Ride ride9 = new Ride();
                ride9.setDriver(dimitris);
                ride9.setFromCity("ΕΜΠ Ζωγράφου");
                ride9.setFromAddress("Πολυτεχνειούπολη Ζωγράφου");
                ride9.setToCity("ΑΣΟΕΕ");
                ride9.setToAddress("Πατησίων 76, Αθήνα");
                ride9.setStartDatetime(LocalDateTime.now().minusDays(2).withHour(18).withMinute(0));
                ride9.setAvailableSeatsTotal(3);
                ride9.setAvailableSeatsRemain(1);
                ride9.setPricePerSeat(new BigDecimal("3.00"));
                ride9.setStatus("COMPLETED");
                ride9.setDistanceKm(new BigDecimal("6.5"));
                ride9.setEstimatedDurationMin(18);
                ride9.setCreatedAt(LocalDateTime.now().minusDays(5));
                rideRepository.save(ride9);

                // Ride 10: Πάντειο -> Οικονομικό (COMPLETED)
                Ride ride10 = new Ride();
                ride10.setDriver(sofia);
                ride10.setFromCity("Πάντειο Πανεπιστήμιο");
                ride10.setFromAddress("Λεωφ. Συγγρού 136, Καλλιθέα");
                ride10.setToCity("Οικονομικό Πανεπιστήμιο Αθηνών");
                ride10.setToAddress("Πατησίων 76, Αθήνα");
                ride10.setStartDatetime(LocalDateTime.now().minusDays(3).withHour(15).withMinute(30));
                ride10.setAvailableSeatsTotal(4);
                ride10.setAvailableSeatsRemain(2);
                ride10.setPricePerSeat(new BigDecimal("4.00"));
                ride10.setStatus("COMPLETED");
                ride10.setDistanceKm(new BigDecimal("7.8"));
                ride10.setEstimatedDurationMin(20);
                ride10.setCreatedAt(LocalDateTime.now().minusDays(6));
                rideRepository.save(ride10);

                // Add some bookings
                Booking b1 = new Booking();
                b1.setRide(ride2);
                b1.setPassenger(maria);
                b1.setSeatsBooked(2);
                b1.setTotalPrice(new BigDecimal("8.00"));
                b1.setStatus("CONFIRMED");
                b1.setBookedAt(LocalDateTime.now().minusHours(3));
                bookingRepository.save(b1);

                Booking b2 = new Booking();
                b2.setRide(ride3);
                b2.setPassenger(dimitris);
                b2.setSeatsBooked(2);
                b2.setTotalPrice(new BigDecimal("70.00"));
                b2.setStatus("CONFIRMED");
                b2.setBookedAt(LocalDateTime.now().minusHours(12));
                bookingRepository.save(b2);

                Booking b3 = new Booking();
                b3.setRide(ride7);
                b3.setPassenger(sofia);
                b3.setSeatsBooked(2);
                b3.setTotalPrice(new BigDecimal("7.60"));
                b3.setStatus("CONFIRMED");
                b3.setBookedAt(LocalDateTime.now().minusHours(6));
                bookingRepository.save(b3);

                Booking b4 = new Booking();
                b4.setRide(ride9);
                b4.setPassenger(maria);
                b4.setSeatsBooked(2);
                b4.setTotalPrice(new BigDecimal("6.00"));
                b4.setStatus("COMPLETED");
                b4.setBookedAt(LocalDateTime.now().minusDays(4));
                bookingRepository.save(b4);

                Booking b5 = new Booking();
                b5.setRide(ride10);
                b5.setPassenger(nikos);
                b5.setSeatsBooked(2);
                b5.setTotalPrice(new BigDecimal("8.00"));
                b5.setStatus("COMPLETED");
                b5.setBookedAt(LocalDateTime.now().minusDays(5));
                bookingRepository.save(b5);

                // Add reviews for completed rides
                if (reviewRepository.count() == 0) {
                    Review r1 = new Review();
                    r1.setRide(ride9);
                    r1.setReviewer(maria);
                    r1.setReviewee(dimitris);
                    r1.setRating(5);
                    r1.setComment("Άριστος οδηγός! Πολύ ευγενικός και έφτασε στην ώρα του.");
                    r1.setRoleOfReviewee("DRIVER");
                    r1.setCreatedAt(LocalDateTime.now().minusDays(1));
                    reviewRepository.save(r1);

                    Review r2 = new Review();
                    r2.setRide(ride9);
                    r2.setReviewer(dimitris);
                    r2.setReviewee(maria);
                    r2.setRating(5);
                    r2.setComment("Εξαιρετική επιβάτης, συνιστάται ανεπιφύλακτα!");
                    r2.setRoleOfReviewee("PASSENGER");
                    r2.setCreatedAt(LocalDateTime.now().minusDays(1));
                    reviewRepository.save(r2);

                    Review r3 = new Review();
                    r3.setRide(ride10);
                    r3.setReviewer(nikos);
                    r3.setReviewee(sofia);
                    r3.setRating(4);
                    r3.setComment("Πολύ καλή διαδρομή, συνεπής οδηγός.");
                    r3.setRoleOfReviewee("DRIVER");
                    r3.setCreatedAt(LocalDateTime.now().minusDays(2));
                    reviewRepository.save(r3);

                    Review r4 = new Review();
                    r4.setRide(ride10);
                    r4.setReviewer(sofia);
                    r4.setReviewee(nikos);
                    r4.setRating(5);
                    r4.setComment("Ευχάριστη συντροφιά!");
                    r4.setRoleOfReviewee("PASSENGER");
                    r4.setCreatedAt(LocalDateTime.now().minusDays(2));
                    reviewRepository.save(r4);
                }


            }
        };
    }
}