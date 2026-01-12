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
import org.example.greenride.entity.User;
import org.example.greenride.mapper.RideMapper;
import org.example.greenride.service.AuthService;
import org.example.greenride.service.BookingService;
import org.example.greenride.service.RideService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final AuthService authService;
    private final RideService rideService;
    private final BookingService bookingService;
    private final org.example.greenride.service.ReviewService reviewService;
    private final org.example.greenride.repository.UserRepository userRepository;
    private final org.example.greenride.service.UserReportService userReportService;

    public WebController(AuthService authService, RideService rideService, BookingService bookingService, org.example.greenride.service.ReviewService reviewService, org.example.greenride.repository.UserRepository userRepository, org.example.greenride.service.UserReportService userReportService) {
        this.authService = authService;
        this.rideService = rideService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.userReportService = userReportService;
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

        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                java.math.BigDecimal driverAvg = user.getRatingAvgDriver() != null ? user.getRatingAvgDriver() : java.math.BigDecimal.ZERO;
                java.math.BigDecimal passengerAvg = user.getRatingAvgPassenger() != null ? user.getRatingAvgPassenger() : java.math.BigDecimal.ZERO;
                
                // Show average of both if they have both, or just the one that is not zero
                java.math.BigDecimal totalAvg;
                if (driverAvg.compareTo(java.math.BigDecimal.ZERO) > 0 && passengerAvg.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    totalAvg = driverAvg.add(passengerAvg).divide(new java.math.BigDecimal("2"), 2, java.math.RoundingMode.HALF_UP);
                } else if (driverAvg.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    totalAvg = driverAvg;
                } else {
                    totalAvg = passengerAvg;
                }
                model.addAttribute("userRatingAvg", totalAvg);
            }

            List<Ride> myRides = new ArrayList<>();
            long totalPending = 0;

            // Fetch rides offered by the user (regardless of role)
            myRides = rideService.getRidesByDriver(userId);
            if (myRides != null && !myRides.isEmpty()) {
                // Calculate pending bookings
                for (Ride ride : myRides) {
                    try {
                        List<Booking> bookings = bookingService.getBookingsByRideId(ride.getId());
                        if (bookings != null) {
                            totalPending += bookings.stream()
                                    .filter(b -> b != null && "PENDING".equals(b.getStatus()))
                                    .count();
                        }
                    } catch (Exception e) {
                        // Ignore errors for individual rides
                        System.out.println("Error checking bookings for ride " + ride.getId() + ": " + e.getMessage());
                    }
                }

                model.addAttribute("myRides", myRides.stream()
                        .map(RideMapper::toResponseDTO)
                        .collect(Collectors.toList()));
            }

            // Store pending count in session for sidebar
            session.setAttribute("pendingBookingsCount", totalPending);
            model.addAttribute("pendingBookingsCount", totalPending);

            // For passengers: show their bookings
            if ("PASSENGER".equals(role) || "USER".equals(role) || "DRIVER".equals(role)) {
                try {
                    List<Booking> myBookings = bookingService.getBookingsByPassengerId(userId);
                    model.addAttribute("myBookings", myBookings != null ? myBookings : new ArrayList<>());
                } catch (Exception e) {
                    System.err.println("Error loading passenger bookings: " + e.getMessage());
                    model.addAttribute("myBookings", new ArrayList<>());
                }
            }

        } catch (Exception e) {
            System.err.println("Error in dashboard: " + e.getMessage());
            // Set defaults
            model.addAttribute("myRides", new ArrayList<>());
            model.addAttribute("myBookings", new ArrayList<>());
            model.addAttribute("pendingBookingsCount", 0L);
        }

        return "dashboard";
    }

    // === MY RIDES - FOR DRIVERS (GET) ===
    @GetMapping("/web/my-rides")
    public String showMyRides(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long driverId = (Long) session.getAttribute("userId");
        if (driverId == null) return "redirect:/web/auth/login";

        try {
            System.out.println("Loading my rides for driver ID: " + driverId);

            // Get driver's rides
            List<Ride> myRides = rideService.getRidesByDriver(driverId);
            System.out.println("Found " + myRides.size() + " rides");

            if (myRides.isEmpty()) {
                model.addAttribute("myRides", new ArrayList<>());
                model.addAttribute("rideBookingsMap", new HashMap<>());
                model.addAttribute("pendingCountsMap", new HashMap<>());
                return "ride/my-rides";
            }

            // For each ride, get bookings and count pending
            Map<Long, List<Booking>> rideBookingsMap = new HashMap<>();
            Map<Long, Long> pendingCountsMap = new HashMap<>();

            for (Ride ride : myRides) {
                try {
                    List<Booking> bookings = bookingService.getBookingsByRideId(ride.getId());
                    System.out.println("Ride " + ride.getId() + " has " + bookings.size() + " bookings");

                    // Handle null bookings list
                    if (bookings == null) {
                        bookings = new ArrayList<>();
                    }

                    rideBookingsMap.put(ride.getId(), bookings);

                    // Count pending bookings for this ride
                    long pendingCount = bookings.stream()
                            .filter(b -> b != null && "PENDING".equals(b.getStatus()))
                            .count();
                    pendingCountsMap.put(ride.getId(), pendingCount);
                } catch (Exception e) {
                    System.err.println("Error loading bookings for ride " + ride.getId() + ": " + e.getMessage());
                    rideBookingsMap.put(ride.getId(), new ArrayList<>());
                    pendingCountsMap.put(ride.getId(), 0L);
                }
            }

            model.addAttribute("myRides", myRides);
            model.addAttribute("rideBookingsMap", rideBookingsMap);
            model.addAttribute("pendingCountsMap", pendingCountsMap);
            model.addAttribute("driverId", driverId);

            return "ride/my-rides";

        } catch (Exception e) {
            System.err.println("Error in showMyRides: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error loading your rides: " + e.getMessage());
            return "redirect:/web/dashboard";
        }
    }

    // === UPDATE BOOKING STATUS (for specific ride) ===
    @PostMapping("/web/my-rides/{rideId}/bookings/{bookingId}/status")
    public String updateBookingStatusFromRide(@PathVariable Long rideId,
                                              @PathVariable Long bookingId,
                                              @RequestParam String status,
                                              HttpSession session,
                                              RedirectAttributes redirectAttributes) {
        try {
            Long driverId = (Long) session.getAttribute("userId");
            if (driverId == null) return "redirect:/web/auth/login";

            // Verify the ride belongs to this driver
            Ride ride = rideService.getRideById(rideId);
            if (!ride.getDriver().getId().equals(driverId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You can only manage bookings for your own rides");
                return "redirect:/web/my-rides";
            }

            // Update booking status
            bookingService.updateBookingStatus(bookingId, status, driverId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking status updated to " + status);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to update status: " + e.getMessage());
        }

        return "redirect:/web/my-rides#ride-" + rideId;
    }

    // === COMPLETE RIDE ===
    @PostMapping("/web/my-rides/{rideId}/complete")
    public String completeRide(@PathVariable Long rideId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            Long driverId = (Long) session.getAttribute("userId");
            if (driverId == null) return "redirect:/web/auth/login";

            Ride ride = rideService.getRideById(rideId);

            // Verify driver owns this ride
            if (!ride.getDriver().getId().equals(driverId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Only the ride driver can complete the ride");
                return "redirect:/web/my-rides";
            }

            // Update ride status to COMPLETED
            rideService.completeRide(rideId);

            // Auto-complete confirmed bookings and reject pending ones
            List<Booking> rideBookings = bookingService.getBookingsByRideId(rideId);
            for (Booking b : rideBookings) {
                if ("CONFIRMED".equals(b.getStatus())) {
                    bookingService.updateBookingStatus(b.getId(), "COMPLETED", driverId);
                } else if ("PENDING".equals(b.getStatus())) {
                    bookingService.updateBookingStatus(b.getId(), "REJECTED", driverId);
                }
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Ride marked as completed! All confirmed bookings are now completed.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to complete ride: " + e.getMessage());
        }

        return "redirect:/web/my-rides";
    }


    // === CANCEL RIDE (driver) ===
    @PostMapping("/web/rides/{rideId}/cancel")
    public String cancelRide(@PathVariable Long rideId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            Long driverId = (Long) session.getAttribute("userId");
            if (driverId == null) return "redirect:/web/auth/login";

            Ride ride = rideService.getRideById(rideId);

            // Verify driver owns this ride
            if (!ride.getDriver().getId().equals(driverId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Only the ride driver can cancel the ride");
                return "redirect:/web/my-rides";
            }

            // Check if ride can be cancelled (only PLANNED rides)
            if (!"PLANNED".equals(ride.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Only PLANNED rides can be cancelled");
                return "redirect:/web/my-rides";
            }

            // Cancel all bookings for this ride
            List<Booking> rideBookings = bookingService.getBookingsByRideId(rideId);
            for (Booking booking : rideBookings) {
                if (!"CANCELLED".equals(booking.getStatus())) {
                    bookingService.cancelBooking(booking.getId());
                }
            }

            // Update ride status to CANCELLED
            rideService.cancelRide(rideId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Ride cancelled successfully! All bookings have been cancelled.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to cancel ride: " + e.getMessage());
        }

        return "redirect:/web/my-rides";
    }

    // === VIEW RIDE DETAILS - UPDATED ===
    @GetMapping("/web/rides/{rideId}")
    public String viewRide(@PathVariable Long rideId,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) return "redirect:/web/auth/login";

            Ride ride = rideService.getRideById(rideId);
            System.out.println("Found ride: " + ride.getId() + " from " + ride.getFromCity() + " to " + ride.getToCity());

            // Get bookings for this ride
            List<Booking> bookings = bookingService.getBookingsByRideId(rideId);
            System.out.println("Found " + bookings.size() + " bookings for this ride");

            // Check if current user is the driver
            boolean isDriver = ride.getDriver().getId().equals(userId);
            System.out.println("Is current user driver? " + isDriver);

            // Check if user already booked this ride (only if not driver)
            boolean alreadyBooked = false;
            if (!isDriver) {
                List<Booking> userBookings = bookingService.getBookingsByPassengerId(userId);
                alreadyBooked = userBookings.stream()
                        .anyMatch(b -> b.getRide().getId().equals(rideId) &&
                                !"CANCELLED".equals(b.getStatus()));
            }

            model.addAttribute("ride", ride);
            model.addAttribute("bookings", bookings);
            model.addAttribute("isDriver", isDriver);
            model.addAttribute("alreadyBooked", alreadyBooked);
            model.addAttribute("canBook", !alreadyBooked && !isDriver &&
                    ride.getAvailableSeatsRemain() > 0 &&
                    "PLANNED".equals(ride.getStatus()));

            return "ride/details";
        } catch (Exception e) {
            System.err.println("Error in viewRide: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Ride not found: " + e.getMessage());
            return "redirect:/web/rides";
        }
    }

    // === EDIT RIDE FORM (GET) ===
    @GetMapping("/web/rides/{rideId}/edit")
    public String editRideForm(@PathVariable Long rideId,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Long driverId = (Long) session.getAttribute("userId");
            if (driverId == null) return "redirect:/web/auth/login";

            Ride ride = rideService.getRideById(rideId);

            // Verify driver owns this ride
            if (!ride.getDriver().getId().equals(driverId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You can only edit your own rides");
                return "redirect:/web/my-rides";
            }

            // Check if ride can be edited (only PLANNED rides)
            if (!"PLANNED".equals(ride.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Only PLANNED rides can be edited");
                return "redirect:/web/my-rides";
            }

            RideRequestDTO rideRequest = new RideRequestDTO();
            rideRequest.setDriverId(driverId);
            rideRequest.setFromCity(ride.getFromCity());
            rideRequest.setFromAddress(ride.getFromAddress());
            rideRequest.setToCity(ride.getToCity());
            rideRequest.setToAddress(ride.getToAddress());
            rideRequest.setStartDatetime(ride.getStartDatetime());
            rideRequest.setEndDatetime(ride.getEndDatetime());
            rideRequest.setAvailableSeatsTotal(ride.getAvailableSeatsTotal());
            rideRequest.setPricePerSeat(ride.getPricePerSeat());
            rideRequest.setDistanceKm(ride.getDistanceKm());
            rideRequest.setEstimatedDurationMin(ride.getEstimatedDurationMin());
            //rideRequest.setWeatherSummary(ride.getWeatherSummary());

            model.addAttribute("rideRequest", rideRequest);
            model.addAttribute("rideId", rideId);

            return "ride/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ride not found");
            return "redirect:/web/my-rides";
        }
    }

    // === UPDATE RIDE (POST) ===
    @PostMapping("/web/rides/{rideId}")
    public String updateRide(@PathVariable Long rideId,
                             @Valid @ModelAttribute("rideRequest") RideRequestDTO request,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "ride/edit";
        }

        try {
            Long driverId = (Long) session.getAttribute("userId");
            if (driverId == null) return "redirect:/web/auth/login";

            // Set driver ID from session
            request.setDriverId(driverId);

            // Verify driver owns this ride before updating
            Ride existingRide = rideService.getRideById(rideId);
            if (!existingRide.getDriver().getId().equals(driverId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You can only edit your own rides");
                return "redirect:/web/my-rides";
            }

            // Update the ride
            rideService.updateRide(rideId, request);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Ride updated successfully!");
            return "redirect:/web/my-rides";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to update ride: " + e.getMessage());
            return "ride/edit";
        }
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

    // === BOOKINGS - NEW (GET: Show form) - ENHANCED ===
    @GetMapping("/web/bookings/new/{rideId}")
    public String newBookingForm(@PathVariable Long rideId,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Long passengerId = (Long) session.getAttribute("userId");
            if (passengerId == null) return "redirect:/web/auth/login";

            // Get ride details
            Ride ride = rideService.getRideById(rideId);

            // Check if user is the driver
            if (ride.getDriver().getId().equals(passengerId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot book your own ride");
                return "redirect:/web/rides";
            }

            // Check seat availability
            if (ride.getAvailableSeatsRemain() <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "This ride is fully booked");
                return "redirect:/web/rides";
            }

            // Check if user already booked this ride
            List<Booking> existingBookings = bookingService.getBookingsByPassengerId(passengerId);
            boolean alreadyBooked = existingBookings.stream()
                    .anyMatch(b -> b.getRide().getId().equals(rideId) &&
                            !"CANCELLED".equals(b.getStatus()));

            if (alreadyBooked) {
                redirectAttributes.addFlashAttribute("errorMessage", "You already have a booking for this ride");
                return "redirect:/web/rides";
            }

            BookingRequestDTO bookingRequest = new BookingRequestDTO();
            bookingRequest.setRideId(rideId);
            bookingRequest.setPassengerId(passengerId);

            model.addAttribute("bookingRequest", bookingRequest);
            model.addAttribute("ride", ride);
            model.addAttribute("maxSeats", ride.getAvailableSeatsRemain());

            return "booking/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ride not found");
            return "redirect:/web/rides";
        }
    }

    // === BOOKINGS - CREATE (POST: Process form) - ENHANCED ===
    @PostMapping("/web/bookings")
    public String createBooking(@Valid @ModelAttribute("bookingRequest") BookingRequestDTO request,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please check your booking details");
            return "redirect:/web/bookings/new/" + request.getRideId();
        }

        try {
            Long currentUserId = (Long) session.getAttribute("userId");
            if (currentUserId == null) return "redirect:/web/auth/login";

            // Verify it's the current user making the booking
            if (!currentUserId.equals(request.getPassengerId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You can only book for yourself");
                return "redirect:/web/bookings/new/" + request.getRideId();
            }

            // Validate seat count
            Ride ride = rideService.getRideById(request.getRideId());
            if (ride.getAvailableSeatsRemain() < request.getSeatsRequested()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Not enough seats available. Only " + ride.getAvailableSeatsRemain() + " seats left.");
                return "redirect:/web/bookings/new/" + request.getRideId();
            }

            // Check if seats requested is valid
            if (request.getSeatsRequested() <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Please request at least 1 seat");
                return "redirect:/web/bookings/new/" + request.getRideId();
            }

            bookingService.createBooking(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking created successfully! You've booked " + request.getSeatsRequested() + " seat(s).");
            return "redirect:/web/bookings/list";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/bookings/new/" + request.getRideId();
        }
    }

    // === BOOKINGS - MY BOOKINGS (GET: Enhanced) ===
    @GetMapping("/web/bookings/list")
    public String showMyBookings(HttpSession session, Model model) {
        Long passengerId = (Long) session.getAttribute("userId");
        if (passengerId == null) return "redirect:/web/auth/login";

        List<Booking> bookings = bookingService.getBookingsByPassengerId(passengerId);
        model.addAttribute("bookings", bookings);
        model.addAttribute("username", session.getAttribute("username"));

        return "booking/list";
    }

    // === BOOKINGS - CANCEL (POST: New method) ===
    @PostMapping("/web/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Long passengerId = (Long) session.getAttribute("userId");
            if (passengerId == null) return "redirect:/web/auth/login";

            Booking booking = bookingService.getBookingById(bookingId);

            // Authorization check
            if (!booking.getPassenger().getId().equals(passengerId)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You can only cancel your own bookings");
                return "redirect:/web/bookings/list";
            }

            // Check if already cancelled
            if ("CANCELLED".equals(booking.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Booking is already cancelled");
                return "redirect:/web/bookings/list";
            }

            // Check if ride already started
            if (booking.getRide().getStartDatetime().isBefore(java.time.LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot cancel a ride that has already started");
                return "redirect:/web/bookings/list";
            }

            bookingService.cancelBooking(bookingId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking cancelled successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error cancelling booking: " + e.getMessage());
        }

        return "redirect:/web/bookings/list";
    }

    // === BOOKING DETAILS (GET: New method) ===
    @GetMapping("/web/bookings/{bookingId}")
    public String viewBookingDetails(@PathVariable Long bookingId,
                                     HttpSession session,
                                     Model model) {
        try {
            Long passengerId = (Long) session.getAttribute("userId");
            if (passengerId == null) return "redirect:/web/auth/login";

            Booking booking = bookingService.getBookingById(bookingId);

            // Authorization check
            if (!booking.getPassenger().getId().equals(passengerId)) {
                return "redirect:/web/bookings/list?error=unauthorized";
            }

            // Get all bookings for this ride (for driver view, though this is passenger view)
            List<Booking> rideBookings = bookingService.getBookingsByRideId(booking.getRide().getId());

            model.addAttribute("booking", booking);
            model.addAttribute("ride", booking.getRide());
            model.addAttribute("bookings", rideBookings);
            model.addAttribute("isDriver", booking.getRide().getDriver().getId().equals(passengerId));
            model.addAttribute("alreadyBooked", true); // Since we are viewing a booking
            model.addAttribute("canBook", false);
            model.addAttribute("username", session.getAttribute("username"));

            return "booking/details";
        } catch (Exception e) {
            return "redirect:/web/bookings/list?error=not-found";
        }
    }

    // =========================
    // REVIEWS
    // =========================
    @GetMapping("/web/reviews/new")
    public String newReviewForm(@RequestParam Long rideId,
                                @RequestParam Long revieweeId,
                                @RequestParam String role,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long reviewerId = (Long) session.getAttribute("userId");
        if (reviewerId == null) return "redirect:/web/auth/login";

        try {
            Ride ride = rideService.getRideById(rideId);
            if (!"COMPLETED".equals(ride.getStatus()) && !"CANCELLED".equals(ride.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Reviews can only be made after a ride is completed or cancelled.");
                return "redirect:/web/dashboard";
            }

            // Check if reviewer is part of the ride
            boolean isDriver = ride.getDriver().getId().equals(reviewerId);
            boolean isPassenger = bookingService.getBookingsByRideId(rideId).stream()
                    .anyMatch(b -> b.getPassenger().getId().equals(reviewerId) && "COMPLETED".equals(b.getStatus()));

            if (!isDriver && !isPassenger) {
                redirectAttributes.addFlashAttribute("errorMessage", "You were not part of this ride.");
                return "redirect:/web/dashboard";
            }

            org.example.greenride.dto.review.ReviewRequestDTO reviewRequest = new org.example.greenride.dto.review.ReviewRequestDTO();
            reviewRequest.setRideId(rideId);
            reviewRequest.setReviewerId(reviewerId);
            reviewRequest.setRevieweeId(revieweeId);
            reviewRequest.setRoleOfReviewee(role);

            model.addAttribute("reviewRequest", reviewRequest);
            model.addAttribute("ride", ride);
            model.addAttribute("reviewee", userRepository.findById(revieweeId).orElse(null));

            return "review/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading review form: " + e.getMessage());
            return "redirect:/web/dashboard";
        }
    }

    @PostMapping("/web/reviews")
    public String createReview(@Valid @ModelAttribute("reviewRequest") org.example.greenride.dto.review.ReviewRequestDTO request,
                               BindingResult result,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "review/new";
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) return "redirect:/web/auth/login";
            request.setReviewerId(userId);

            reviewService.createReview(request);
            redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully!");
            return "redirect:/web/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit review: " + e.getMessage());
            return "review/new";
        }
    }

    @GetMapping("/web/reviews")
    public String showMyReviews(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/web/auth/login";

        List<org.example.greenride.entity.Review> reviews = reviewService.getReviewsByUserId(userId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("userId", userId);
        model.addAttribute("isAdminView", false);
        return "review/list";
    }

    // =========================
    // REPORTS - Submit User Report
    // =========================
    @PostMapping("/web/reports/submit")
    @ResponseBody
    public String submitUserReport(@RequestParam Long reportedUserId,
                                   @RequestParam String reason,
                                   HttpSession session) {
        try {
            Long reporterId = (Long) session.getAttribute("userId");
            if (reporterId == null) {
                return "Unauthorized";
            }

            // Cannot report yourself
            if (reporterId.equals(reportedUserId)) {
                return "You cannot report yourself";
            }

            org.example.greenride.dto.userreport.UserReportRequestDTO reportDTO =
                new org.example.greenride.dto.userreport.UserReportRequestDTO();
            reportDTO.setReportedUserId(reportedUserId);
            reportDTO.setReporterUserId(reporterId);
            reportDTO.setReason(reason);

            userReportService.createReport(reportDTO);
            return "OK";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // =========================
    // REPORTS - My Reports (GET: Show user's submitted reports)
    // =========================
    @GetMapping("/web/reports/my-reports")
    public String showMyReports(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/web/auth/login";

        try {
            List<org.example.greenride.entity.UserReport> reports =
                userReportService.getAll().stream()
                    .filter(report -> report.getReporterUser() != null &&
                                    report.getReporterUser().getId().equals(userId))
                    .collect(Collectors.toList());

            model.addAttribute("reports", reports);
            return "report/my-reports";
        } catch (Exception e) {
            model.addAttribute("reports", new ArrayList<>());
            return "report/my-reports";
        }
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
    @GetMapping("/debug/session")
    @ResponseBody
    public String debugSession(HttpSession session) {
        return "User ID: " + session.getAttribute("userId") + "\n" +
                "Username: " + session.getAttribute("username") + "\n" +
                "Role: " + session.getAttribute("role") + "\n" +
                "Pending Count: " + session.getAttribute("pendingBookingsCount");
    }

    @GetMapping("/debug/ride/{id}")
    @ResponseBody
    public String debugRide(@PathVariable Long id) {
        try {
            Ride ride = rideService.getRideById(id);
            return "Ride ID: " + ride.getId() + "\n" +
                    "Fields: " + ride.getClass().getDeclaredFields().length + "\n" +
                    "Available fields:\n" +
                    "- fromCity: " + ride.getFromCity() + "\n" +
                    "- toCity: " + ride.getToCity() + "\n" +
                    "- pricePerSeat: " + ride.getPricePerSeat() + "\n" +
                    "- status: " + ride.getStatus() + "\n" +
                    "- driver: " + (ride.getDriver() != null ? ride.getDriver().getUsername() : "null") + "\n" +
                    "- availableSeatsRemain: " + ride.getAvailableSeatsRemain() + "\n" +
                    "- availableSeatsTotal: " + ride.getAvailableSeatsTotal() + "\n" +
                    "- startDatetime: " + ride.getStartDatetime() + "\n" +
                    "- distanceKm: " + ride.getDistanceKm() + "\n" +
                    "- weatherSummary: " + ride.getWeatherSummary();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}