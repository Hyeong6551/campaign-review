package edu.du.gatewayservice.controller;

import edu.du.gatewayservice.dto.LoginRequest;
import edu.du.gatewayservice.dto.LoginResponse;
import edu.du.gatewayservice.dto.RegisterRequest;
import edu.du.gatewayservice.entity.Role;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import edu.du.gatewayservice.service.AuthService;
import edu.du.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return Mono.just(authService.login(loginRequest))
                .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody RegisterRequest registerRequest) {
        return Mono.just(registerRequest)
                .flatMap(request -> {
                    if (userRepository.findById(request.getId()).isPresent()) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }

                    User user = new User();
                    user.setId(request.getId());
                    user.setNickname(request.getNickname());
                    user.setEmail(request.getEmail());
                    user.setPhone(request.getPhone());
                    user.setPost_url(request.getPost_url());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setCreated_at(now());
                    user.setRole(Role.USER);

                    userRepository.save(user);
                    return Mono.just(ResponseEntity.ok().build());
                });
    }

    @GetMapping("/current")
    public Mono<ResponseEntity<?>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            return Mono.just(userRepository.findById(username)
                    .map(user -> ResponseEntity.ok(new LoginResponse(token, user.getId(), user.getRole().value(), user.getEmail())))
                    .orElse(ResponseEntity.notFound().build()));
        }
        return Mono.just(ResponseEntity.notFound().build());
    }
} 