package edu.du.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDto {
    private Long userNo;
    private String id;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String post_url;
    private LocalDateTime created_at;
}
