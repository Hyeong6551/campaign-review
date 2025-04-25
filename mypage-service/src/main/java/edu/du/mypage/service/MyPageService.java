package edu.du.mypage.service;

import edu.du.mypage.config.RabbitMQUserConfig;
import edu.du.mypage.dto.MyPageDto;
import edu.du.mypage.dto.UserDeleteRequest;
import edu.du.mypage.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final RestTemplate restTemplate;
    private final AmqpTemplate rabbitTemplate;

    private static final String GATEWAY_URL = "http://gateway-service/api/user/";

    public MyPageDto getMyInfo(Long userNo) {
        String url = GATEWAY_URL + userNo;
        return restTemplate.getForObject(url, MyPageDto.class);
    }

    public void sendUpdate(UserUpdateRequest message) {
        rabbitTemplate.convertAndSend(RabbitMQUserConfig.EXCHANGE, "user.update", message);
    }

    public void sendDelete(UserDeleteRequest message) {
        rabbitTemplate.convertAndSend(RabbitMQUserConfig.EXCHANGE, "user.delete", message);
    }
}
