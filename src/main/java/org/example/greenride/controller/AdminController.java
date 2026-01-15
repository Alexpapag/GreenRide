package org.example.greenride.controller;

import jakarta.servlet.http.HttpSession;
import org.example.greenride.entity.User;
import org.example.greenride.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Web Controller για διαχειριστικές λειτουργίες (admin panel)
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final org.example.greenride.service.ReviewService reviewService;

    public AdminController(AdminService adminService, org.example.greenride.service.ReviewService reviewService) {
        this.adminService = adminService;
        this.reviewService = reviewService;
    }

    // Έλεγχος αν ο χρήστης είναι admin
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.equalsIgnoreCase("ADMIN");
    }

    // Admin Dashboard - στατιστικά συστήματος
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/web/auth/login";
        }

        if (!isAdmin(session)) {
            return "redirect:/web/dashboard";
        }

        Map<String, Object> stats = adminService.getDashboardStats();
        model.addAllAttributes(stats);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));

        return "admin/dashboard";
    }

    // Διαχείριση χρηστών - προβολή όλων
    @GetMapping("/users")
    public String users(HttpSession session, Model model,
                        @RequestParam(required = false) String search) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("users", adminService.searchUsers(search));
            model.addAttribute("searchQuery", search);
        } else {
            model.addAttribute("users", adminService.getAllUsers());
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "admin/users";
    }

    // Ενημέρωση κατάστασης χρήστη (ACTIVE/BANNED/SUSPENDED)
    @PostMapping("/users/{id}/status")
    public String updateUserStatus(@PathVariable Long id,
                                   @RequestParam String status,
                                   HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        adminService.updateUserStatus(id, status);
        return "redirect:/admin/users";
    }

    // Ενημέρωση ρόλου χρήστη
    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam String role,
                                 HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        adminService.updateUserRole(id, role);
        return "redirect:/admin/users";
    }

    // Διαχείριση διαδρομών - προβολή όλων
    @GetMapping("/rides")
    public String rides(HttpSession session, Model model,
                        @RequestParam(required = false) String status) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        if (status != null && !status.trim().isEmpty()) {
            model.addAttribute("rides", adminService.getRidesByStatus(status));
            model.addAttribute("selectedStatus", status);
        } else {
            model.addAttribute("rides", adminService.getAllRides());
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "admin/rides";
    }

    // Ενημέρωση κατάστασης διαδρομής
    @PostMapping("/rides/{id}/status")
    public String updateRideStatus(@PathVariable Long id,
                                   @RequestParam String status,
                                   HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        adminService.updateRideStatus(id, status);
        return "redirect:/admin/rides";
    }

    // Διαχείριση αναφορών - προβολή όλων
    @GetMapping("/reports")
    public String reports(HttpSession session, Model model,
                          @RequestParam(required = false) String status) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        if (status != null && !status.trim().isEmpty()) {
            model.addAttribute("reports", adminService.getReportsByStatus(status));
            model.addAttribute("selectedStatus", status);
        } else {
            model.addAttribute("reports", adminService.getAllReports());
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "admin/reports";
    }

    // Ενημέρωση κατάστασης αναφοράς
    @PostMapping("/reports/{id}/status")
    public String updateReportStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        adminService.updateReportStatus(id, status);
        return "redirect:/admin/reports";
    }

    // Διαχείριση αξιολογήσεων - προβολή όλων
    @GetMapping("/reviews")
    public String reviews(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        model.addAttribute("reviews", reviewService.getAllReviews());
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("isAdminView", true);
        model.addAttribute("username", session.getAttribute("username"));
        return "review/list";
    }

    // Διαγραφή προβληματικής αξιολόγησης
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/web/auth/login";
        }

        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }
}