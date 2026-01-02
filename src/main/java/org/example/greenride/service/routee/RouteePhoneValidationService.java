package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteePhoneValidationResponseDTO;

public interface RouteePhoneValidationService {
    RouteePhoneValidationResponseDTO validate(String phoneE164);
}
