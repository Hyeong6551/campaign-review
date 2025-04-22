package edu.du.gatewayservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String id;
    private String password;
} 