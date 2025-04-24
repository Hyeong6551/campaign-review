package edu.du.mypage.controller;

import edu.du.mypage.dto.MyPageDto;
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
    public ResponseEntity<?> updateUserInfo(@RequestBody UserUpdateRequest message) {
        myPageService.send(message);
        return ResponseEntity.ok("수정 요청 전송 완료");
    }
}
