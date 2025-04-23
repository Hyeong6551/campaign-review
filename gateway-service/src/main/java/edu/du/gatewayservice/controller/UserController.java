package edu.du.gatewayservice.controller;

import edu.du.gatewayservice.dto.RegisterRequest;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userNo}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userNo) {
        try {
            User user = userService.getUserByUserNo(userNo);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
