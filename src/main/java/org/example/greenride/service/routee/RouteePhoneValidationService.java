package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteePhoneValidationResponseDTO;

// Service interface για επικύρωση τηλεφώνων μέσω Routee API
public interface RouteePhoneValidationService {
    // Επικύρωση τηλεφώνου σε E.164 format μέσω Routee API
    RouteePhoneValidationResponseDTO validate(String phoneE164);
}
