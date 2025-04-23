package edu.du.gatewayservice.dto;

import edu.du.gatewayservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    private String id;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String post_url;
}