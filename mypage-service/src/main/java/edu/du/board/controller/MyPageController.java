package edu.du.board.controller;

import edu.du.board.dto.MyPageDto;
import edu.du.board.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService mypageService) {
        this.myPageService = mypageService;
    }

    @GetMapping("/info")
    public ResponseEntity<MyPageDto> getUserInfo(@RequestParam("userNo") Long userNo) {
        MyPageDto userInfo = myPageService.getMyInfo(userNo);
        return ResponseEntity.ok(userInfo);
    }
}
