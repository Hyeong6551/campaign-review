package edu.du.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userNo;
    private String id;
    private String role;
    private String nickname;
} 