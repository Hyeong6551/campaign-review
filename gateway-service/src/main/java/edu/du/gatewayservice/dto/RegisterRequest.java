package edu.du.gatewayservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String id;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String post_url;
} 