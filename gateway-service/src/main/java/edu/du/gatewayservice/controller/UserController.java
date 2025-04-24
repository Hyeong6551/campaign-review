package edu.du.gatewayservice.controller;

import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete/{userNo}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userNo) {
        userService.deleteUser(userNo);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
