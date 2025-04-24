package edu.du.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long no;
    private Long userNo;
    private String nickname;
    private String title;
    private String content;
    private String postURL;
}
