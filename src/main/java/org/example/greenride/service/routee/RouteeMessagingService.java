package org.example.greenride.service.routee;

import org.example.greenride.dto.routee.RouteeSmsAnalyzeRequestDTO;
import org.example.greenride.dto.routee.RouteeSmsAnalyzeResponseDTO;

public interface RouteeMessagingService {
    RouteeSmsAnalyzeResponseDTO analyzeSms(RouteeSmsAnalyzeRequestDTO request);
}
