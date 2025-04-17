package edu.du.gatewayservice.service;

import edu.du.gatewayservice.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Object message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_BOARD,
            "board.message",
            message
        );
    }
} 