package edu.du.gatewayservice.service;

import edu.du.gatewayservice.dto.LoginRequest;
import edu.du.gatewayservice.dto.LoginResponse;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import edu.du.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findById(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token, user.getId(), user.getRole().value(), user.getEmail());
    }
} 