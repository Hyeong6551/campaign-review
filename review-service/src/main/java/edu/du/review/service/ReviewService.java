package edu.du.review.service;

import edu.du.review.dto.ReviewDTO;
import edu.du.review.entity.Review;
import edu.du.review.repository.ReviewRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final Path uploadPath = Paths.get(System.getProperty("user.dir") + "/review-service/images");

    public ReviewService(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
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

            String imageUrl = "/images/" + filename;

            Review review = Review.builder()
                    .userNo(reviewDTO.getUserNo())
                    .nickname(reviewDTO.getNickname())
                    .title(reviewDTO.getTitle())
                    .content(reviewDTO.getContent())
                    .post_url(reviewDTO.getPostUrl())
                    .image_url(imageUrl)
                    .createdDate(LocalDateTime.now())
                    .build();
            System.out.println(review);
            return reviewRepo.save(review);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Transactional
    public Review updateReview(Long reviewId, ReviewDTO reviewDTO, MultipartFile imageFile) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        String imageUrl = review.getImage_url(); // 기본값: 기존 이미지

        // 새 이미지가 있을 경우 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imageUrl = "/images/" + filename;
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setPost_url(reviewDTO.getPostUrl());
        review.setNickname(reviewDTO.getNickname());
        review.setImage_url(imageUrl); // 변경된 이미지 or 기존 이미지
        return reviewRepo.save(review);
    }

    // 리뷰 목록 조회
    public List<Review> findAll() {
        return reviewRepo.findAll();
    }

    // 사용자가 작성한 리뷰 조회
    public List<ReviewDTO> getReviewsByUserNo(Long userNo) {
        List<Review> reviews = reviewRepo.findByUserNo(userNo);
        List<ReviewDTO> reviewDTO = new ArrayList<>();

        for (Review review : reviews) {
            ReviewDTO dto = ReviewDTO.builder()
                    .postNo(review.getPostNo())
                    .nickname(review.getNickname())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .postUrl(review.getPost_url())
                    .build();
            reviewDTO.add(dto);
        }
        return reviewDTO;
    }

    // 단일 리뷰 조회
    public ReviewDTO getReviewByUserNoAndPostNo(Long userNo, Long postNo) {
        Review review = reviewRepo.findByUserNoAndPostNo(userNo, postNo)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않아요"));

        return ReviewDTO.builder()
                .postNo(review.getPostNo())
                .userNo(review.getUserNo())
                .nickname(review.getNickname())
                .title(review.getTitle())
                .content(review.getContent())
                .postUrl(review.getPost_url())
                .image_url(review.getImage_url())
                .build();
    }


    // 리뷰 삭제
    public void deleteReview(Long postNo) {
        if (!reviewRepo.existsById(postNo)) {
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
        }
        reviewRepo.deleteById(postNo);
    }
}
