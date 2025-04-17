package edu.du.gatewayservice.controller;

import edu.du.gatewayservice.dto.LoginRequest;
import edu.du.gatewayservice.dto.LoginResponse;
import edu.du.gatewayservice.dto.RegisterRequest;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import edu.du.gatewayservice.service.AuthService;
import edu.du.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
                    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }

                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setEmail(request.getEmail());
                    user.setRole("USER");

                    userRepository.save(user);
                    return Mono.just(ResponseEntity.ok().build());
                });
    }

    @GetMapping("/current")
    public Mono<ResponseEntity<?>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            return Mono.just(userRepository.findByUsername(username)
                    .map(user -> ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole(), user.getEmail())))
                    .orElse(ResponseEntity.notFound().build()));
        }
        return Mono.just(ResponseEntity.notFound().build());
    }
} 