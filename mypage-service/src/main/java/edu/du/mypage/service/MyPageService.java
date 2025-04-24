package edu.du.mypage.service;

import edu.du.mypage.config.RabbitMQUserConfig;
import edu.du.mypage.dto.MyPageDto;
import edu.du.mypage.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    private static final String GATEWAY_URL = "http://gateway-service/api/user/";

    public MyPageDto getMyInfo(Long userNo) {
        String url = GATEWAY_URL + userNo;
        return restTemplate.getForObject(url, MyPageDto.class);
    }

    public void send(UserUpdateRequest message) {
        rabbitTemplate.convertAndSend(
                RabbitMQUserConfig.EXCHANGE_NAME,
                RabbitMQUserConfig.ROUTING_KEY,
                message
        );
    }
}
