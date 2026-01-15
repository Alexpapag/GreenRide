package org.example.greenride.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// Custom Error Controller - προσαρμοσμένη σελίδα σφαλμάτων
@Controller
public class CustomErrorController implements ErrorController {
    // Χειρισμός όλων των errors - επιστρέφει error.html
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "error";
    }
}
