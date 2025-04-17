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
    private long post_no;

    @Column(nullable = false)
    private String title;
    private String content;
    private String imageURL;
    private String postURL;
    private LocalDateTime createdDate;
}
