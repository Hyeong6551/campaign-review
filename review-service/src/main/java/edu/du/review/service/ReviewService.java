package edu.du.review.service;

import edu.du.review.dto.ReviewDTO;
import edu.du.review.entity.Review;
import edu.du.review.repository.ReviewRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final Path uploadPath = Paths.get("uploads");

    public ReviewService(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    // 목록 조회
    public List<Review> findAll() {
        return reviewRepo.findAll();
    }

    // 리뷰 작성
    @Transactional
    public Review saveReview(ReviewDTO reviewDTO, MultipartFile imageFile) {
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/" + filename;

            Review review = Review.builder()
                    .title(reviewDTO.getTitle())
                    .content(reviewDTO.getContent())
                    .postURL(reviewDTO.getPostURL())
                    .imageURL(imageUrl)
                    .createdDate(LocalDateTime.now())
                    .build();
            return reviewRepo.save(review);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}
