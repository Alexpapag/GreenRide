package org.example.greenride.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.greenride.dto.auth.AuthLoginRequest;
import org.example.greenride.dto.auth.AuthRegisterRequest;
import org.example.greenride.dto.auth.AuthResponse;
import org.example.greenride.dto.booking.BookingRequestDTO;
import org.example.greenride.dto.booking.BookingResponseDTO;
import org.example.greenride.dto.ride.RideRequestDTO;
import org.example.greenride.dto.ride.RideResponseDTO;
import org.example.greenride.dto.user.AuthResponseDTO;
import org.example.greenride.dto.user.UserLoginDTO;
import org.example.greenride.dto.user.UserRegistrationDTO;
import org.example.greenride.entity.Booking;
import org.example.greenride.entity.Ride;
import org.example.greenride.mapper.RideMapper;
import org.example.greenride.service.AuthService;
import org.example.greenride.service.BookingService;
import org.example.greenride.service.RideService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    private final AuthService authService;
    private final RideService rideService;
    private final BookingService bookingService;

    public WebController(AuthService authService, RideService rideService, BookingService bookingService) {
        this.authService = authService;
        this.rideService = rideService;
        this.bookingService = bookingService;
    }

    // =========================
    // AUTH - LOGIN (GET: Show form)
    // =========================
    @GetMapping("/web/auth/login")
    public String loginForm(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        model.addAttribute("loginRequest", new AuthLoginRequest());
        return "auth/login";
    }

    // =========================
    // AUTH - LOGIN (POST: Process form)
    // =========================
    @PostMapping("/web/auth/login")
    public String login(@Valid @ModelAttribute("loginRequest") AuthLoginRequest request,
                        BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Please check your input");
            return "auth/login";
        }

        try {
            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setUsername(request.getUsername());
            loginDTO.setPassword(request.getPassword());
            AuthResponseDTO response = authService.login(loginDTO);

            session.setAttribute("userId", response.getUserId());
            session.setAttribute("username", response.getUsername());
            session.setAttribute("role", response.getRole());
            session.setAttribute("token", response.getToken());

            // Check if user is admin
            if ("ADMIN".equalsIgnoreCase(response.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/web/dashboard";
            }
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("loginRequest", new AuthLoginRequest());
            return "auth/login";
        }
    }

    // =========================
    // AUTH - REGISTER (GET: Show form)
    // =========================
    @GetMapping("/web/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new AuthRegisterRequest());
        return "auth/register";
    }

    // =========================
    // AUTH - REGISTER (POST: Process form)
    // =========================
    @PostMapping("/web/auth/register")
    public String register(@Valid @ModelAttribute("registerRequest") AuthRegisterRequest request,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            UserRegistrationDTO regDTO = new UserRegistrationDTO();
            regDTO.setUsername(request.getUsername());
            regDTO.setEmail(request.getEmail());
            regDTO.setFullName(request.getFullName() != null ? request.getFullName() : request.getUsername());
            regDTO.setPassword(request.getPassword());
            regDTO.setRole("USER"); // Default role

            authService.register(regDTO);
            return "redirect:/web/auth/login?registered=true";
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    // =========================
    // LOGOUT
    // =========================
    @PostMapping("/web/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/web/auth/login?logout=true";
    }

    // =========================
    // DASHBOARD (requires login)
    // =========================
    @GetMapping("/web/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) return "redirect:/web/auth/login";

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", role);

        // For drivers: show their rides
        if ("DRIVER".equals(role)) {
            List<Ride> myRides = rideService.getRidesByDriver(userId);
            model.addAttribute("myRides", myRides.stream().map(RideMapper::toResponseDTO).toList());
        }
        // For passengers: show their bookings
        else {
            List<Booking> myBookings = bookingService.getBookingsByPassengerId(userId);
            model.addAttribute("myBookings", myBookings);
        }
        return "dashboard";
    }

    // =========================
    // RIDES - LIST
    // =========================
    @GetMapping("/web/rides")
    public String rides(Model model) {
        List<RideResponseDTO> rides = rideService.getAllRides()
                .stream().map(RideMapper::toResponseDTO).toList();
        model.addAttribute("rides", rides);
        return "ride/list";
    }

    // =========================
    // RIDES - NEW (GET: Show form)
    // =========================
    @GetMapping("/web/rides/new")
    public String newRideForm(Model model) {
        model.addAttribute("rideRequest", new RideRequestDTO());
        return "ride/new";
    }

    // =========================
    // RIDES - CREATE (POST: Process form)
    // =========================
    @PostMapping("/web/rides")
    public String createRide(@Valid @ModelAttribute("rideRequest") RideRequestDTO request,
                             BindingResult result, HttpSession session, Model model) {
        System.out.println("=== CREATE RIDE START ===");

        // FIRST: Get driverId from session and set it BEFORE validation
        Long driverId = (Long) session.getAttribute("userId");
        if (driverId == null) {
            System.out.println("No user ID in session, redirecting to login");
            return "redirect:/web/auth/login";
        }

        System.out.println("Driver ID from session: " + driverId);
        request.setDriverId(driverId); // SET IT HERE, BEFORE VALIDATION

        System.out.println("Form data received:");
        System.out.println("From City: " + request.getFromCity());
        System.out.println("To City: " + request.getToCity());
        System.out.println("Start Datetime: " + request.getStartDatetime());
        System.out.println("Seats: " + request.getAvailableSeatsTotal());
        System.out.println("Price: " + request.getPricePerSeat());
        System.out.println("Driver ID: " + request.getDriverId());

        // NOW validate
        if (result.hasErrors()) {
            System.out.println("Form has errors:");
            result.getFieldErrors().forEach(error -> {
                System.out.println(" - " + error.getField() + ": " + error.getDefaultMessage());
            });
            return "ride/new";
        }

        try {
            System.out.println("Calling rideService.createRide()...");
            rideService.createRide(request);
            System.out.println("Ride created successfully!");

            return "redirect:/web/rides";
        } catch (Exception e) {
            System.out.println("Error creating ride: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to create ride: " + e.getMessage());
            return "ride/new";
        }
    }

    // =========================
    // BOOKINGS - NEW (GET: Show form)
    // =========================
    @GetMapping("/web/bookings/new/{rideId}")
    public String newBookingForm(@PathVariable Long rideId, Model model, HttpSession session) {
        Long passengerId = (Long) session.getAttribute("userId");
        if (passengerId == null) return "redirect:/web/auth/login";

        BookingRequestDTO booking = new BookingRequestDTO();
        booking.setRideId(rideId);
        booking.setPassengerId(passengerId);
        model.addAttribute("bookingRequest", booking);
        return "booking/new";
    }

    // =========================
    // BOOKINGS - CREATE (POST: Process form)
    // =========================
    @PostMapping("/web/bookings")
    public String createBooking(@Valid @ModelAttribute("bookingRequest") BookingRequestDTO request,
                                BindingResult result) {
        if (result.hasErrors()) return "booking/new";

        bookingService.createBooking(request);
        return "redirect:/web/dashboard";
    }

    // =========================
    // DEBUG ENDPOINT
    // =========================
    @GetMapping("/debug/user-info")
    @ResponseBody
    public String debugUserInfo(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("Session ID: ").append(session.getId()).append("\n");
        sb.append("User ID: ").append(session.getAttribute("userId")).append("\n");
        sb.append("Username: ").append(session.getAttribute("username")).append("\n");
        sb.append("Role: ").append(session.getAttribute("role")).append("\n");
        sb.append("Token: ").append(session.getAttribute("token")).append("\n");

        return sb.toString();
    }
}