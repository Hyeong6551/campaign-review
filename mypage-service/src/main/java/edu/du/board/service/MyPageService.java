package edu.du.board.service;

import edu.du.board.dto.MyPageDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyPageService {

    private final RestTemplate restTemplate;

    public MyPageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String GATEWAY_URL = "http://gateway-service/api/user/";

    public MyPageDto getMyInfo(Long userNo) {
        String url = GATEWAY_URL + userNo;
        return restTemplate.getForObject(url, MyPageDto.class);
    }
}
