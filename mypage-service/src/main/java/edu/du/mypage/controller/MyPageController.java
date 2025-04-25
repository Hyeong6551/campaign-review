package edu.du.mypage.controller;

import edu.du.mypage.dto.MyPageDto;
import edu.du.mypage.dto.UserDeleteRequest;
import edu.du.mypage.dto.UserUpdateRequest;
import edu.du.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/info")
    public ResponseEntity<MyPageDto> getUserInfo(@RequestParam("userNo") Long userNo) {
        MyPageDto userInfo = myPageService.getMyInfo(userNo);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest dto) {
        myPageService.sendUpdate(dto);
        return ResponseEntity.ok("요청 완료");
    }

    @DeleteMapping("/delete/{userNo}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userNo) {
        myPageService.sendDelete(new UserDeleteRequest(userNo));
        return ResponseEntity.ok("탈퇴 요청 전송 완료");
    }
}
