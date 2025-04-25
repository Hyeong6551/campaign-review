package edu.du.review.repository;

import edu.du.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByUserNo(Long userNo);
    Optional<Review> findByUserNoAndPostNo(Long userNo, Long postNo);
}
