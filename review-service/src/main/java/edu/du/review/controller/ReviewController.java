package edu.du.review.controller;

import edu.du.review.dto.ReviewDTO;
import edu.du.review.entity.Review;
import edu.du.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public List<Review> findAll() {
        return reviewService.findAll();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createReview(
            @RequestPart("review") ReviewDTO reviewDTO,
            @RequestPart("image") MultipartFile imageFile) {

        Review PostReview = reviewService.saveReview(reviewDTO, imageFile);
        System.out.println(PostReview);
        return ResponseEntity.ok(PostReview);
    }
}
