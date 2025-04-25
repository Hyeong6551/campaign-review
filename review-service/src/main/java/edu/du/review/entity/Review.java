package edu.du.review.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postNo;
    private Long userNo;
    @Column(nullable = false)
    private String title;
    private String nickname;
    private String content;
    private String image_url;
    private String post_url;
    private LocalDateTime createdDate;
}
