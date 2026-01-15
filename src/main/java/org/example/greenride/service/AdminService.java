package org.example.greenride.service;

import org.example.greenride.entity.Booking;
import org.example.greenride.entity.Ride;
import org.example.greenride.entity.User;
import org.example.greenride.entity.UserReport;
import org.example.greenride.repository.BookingRepository;
import org.example.greenride.repository.RideRepository;
import org.example.greenride.repository.UserReportRepository;
import org.example.greenride.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Service για διαχειριστικές λειτουργίες (dashboard stats, user/ride/report management)
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final BookingRepository bookingRepository;
    private final UserReportRepository userReportRepository;

    public AdminService(UserRepository userRepository,
                        RideRepository rideRepository,
                        BookingRepository bookingRepository,
                        UserReportRepository userReportRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.bookingRepository = bookingRepository;
        this.userReportRepository = userReportRepository;
    }

    // Ανάκτηση στατιστικών για admin dashboard
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus("ACTIVE");
        long bannedUsers = userRepository.countByStatus("BANNED");

        // Ride statistics
        long totalRides = rideRepository.count();
        long activeRides = rideRepository.countByStatus("PLANNED");
        long completedRides = rideRepository.countByStatus("COMPLETED");

        // Booking statistics
        long totalBookings = bookingRepository.count();
        long confirmedBookings = bookingRepository.countByStatus("CONFIRMED");

        // Report statistics
        long openReports = userReportRepository.countByStatus("OPEN");
        long totalReports = userReportRepository.count();

        // Recent activities
        List<Ride> recentRides = rideRepository.findTop5ByOrderByCreatedAtDesc();
        List<Booking> recentBookings = bookingRepository.findTop5ByOrderByBookedAtDesc();
        List<User> recentUsers = userRepository.findTop5ByOrderByCreatedAtDesc();

        // Daily stats
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long todayRides = rideRepository.countByCreatedAtAfter(startOfDay);
        long todayBookings = bookingRepository.countByBookedAtAfter(startOfDay);
        long todayUsers = userRepository.countByCreatedAtAfter(startOfDay);

        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("bannedUsers", bannedUsers);
        stats.put("totalRides", totalRides);
        stats.put("activeRides", activeRides);
        stats.put("completedRides", completedRides);
        stats.put("totalBookings", totalBookings);
        stats.put("confirmedBookings", confirmedBookings);
        stats.put("openReports", openReports);
        stats.put("totalReports", totalReports);
        stats.put("todayRides", todayRides);
        stats.put("todayBookings", todayBookings);
        stats.put("todayUsers", todayUsers);
        stats.put("recentRides", recentRides);
        stats.put("recentBookings", recentBookings);
        stats.put("recentUsers", recentUsers);

        return stats;
    }

    // Ανάκτηση όλων των χρηστών
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Αναζήτηση χρηστών με keyword (username/email/fullName)
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingOrEmailContainingOrFullNameContaining(
                keyword, keyword, keyword);
    }

    // Ενημέρωση status χρήστη (ACTIVE/BANNED/SUSPENDED)
    public User updateUserStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!List.of("ACTIVE", "BANNED", "SUSPENDED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        user.setStatus(status);
        return userRepository.save(user);
    }

    // Ενημέρωση role χρήστη (USER/ADMIN/DRIVER)
    public User updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!List.of("USER", "ADMIN", "DRIVER").contains(role)) {
            throw new IllegalArgumentException("Invalid role");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    // Ανάκτηση όλων των διαδρομών
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    // Ανάκτηση διαδρομών με βάση status
    public List<Ride> getRidesByStatus(String status) {
        return rideRepository.findByStatus(status);
    }

    // Ενημέρωση status διαδρομής
    public Ride updateRideStatus(Long rideId, String status) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        if (!List.of("PLANNED", "ONGOING", "COMPLETED", "CANCELLED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        ride.setStatus(status);
        return rideRepository.save(ride);
    }

    // Ανάκτηση όλων των αναφορών χρηστών
    public List<UserReport> getAllReports() {
        return userReportRepository.findAll();
    }

    // Ανάκτηση αναφορών με βάση status
    public List<UserReport> getReportsByStatus(String status) {
        return userReportRepository.findByStatus(status);
    }

    // Ενημέρωση status αναφοράς (OPEN/REVIEWED/CLOSED)
    public UserReport updateReportStatus(Long reportId, String status) {
        UserReport report = userReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (!List.of("OPEN", "REVIEWED", "CLOSED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        report.setStatus(status);
        report.setResolvedAt(LocalDateTime.now());
        return userReportRepository.save(report);
    }
}