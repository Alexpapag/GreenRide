package org.example.greenride.controller;

import jakarta.validation.Valid;
import org.example.greenride.dto.review.ReviewRequestDTO;
import org.example.greenride.dto.review.ReviewResponseDTO;
import org.example.greenride.entity.Review;
import org.example.greenride.mapper.ReviewMapper;
import org.example.greenride.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDTO create(@Valid @RequestBody ReviewRequestDTO dto) {
        Review created = reviewService.createReview(dto);
        return ReviewMapper.toResponseDTO(created);
    }

    @GetMapping("/{id}")
    public ReviewResponseDTO getById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ReviewMapper.toResponseDTO(review);
    }

    @GetMapping("/ride/{rideId}")
    public List<ReviewResponseDTO> getByRide(@PathVariable Long rideId) {
        return reviewService.getReviewsByRideId(rideId)
                .stream()
                .map(ReviewMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponseDTO> getByUser(@PathVariable Long userId) {
        return reviewService.getReviewsByUserId(userId)
                .stream()
                .map(ReviewMapper::toResponseDTO)
                .toList();
    }

    @PutMapping("/{id}")
    public ReviewResponseDTO update(@PathVariable Long id,
                                    @Valid @RequestBody ReviewRequestDTO dto) {
        Review updated = reviewService.updateReview(id, dto);
        return ReviewMapper.toResponseDTO(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
