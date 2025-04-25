package edu.du.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long postNo;
    private Long userNo;
    private String nickname;
    private String title;
    private String content;
    private String postUrl;
    private String image_url;
}
