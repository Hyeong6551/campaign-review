package edu.du.gatewayservice.service;

import edu.du.gatewayservice.config.RabbitMQConsumerConfig;
import edu.du.gatewayservice.dto.UserUpdateRequest;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUserByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @RabbitListener(queues = RabbitMQConsumerConfig.QUEUE_NAME)
    public void receive(UserUpdateRequest msg) {
        User user = userRepository.findById(msg.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        user.setNickname(msg.getNickname());
        user.setEmail(msg.getEmail());
        user.setPhone(msg.getPhone());

        if (msg.getNewPassword() != null && !msg.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(msg.getNewPassword()));
        }

        userRepository.save(user);
        System.out.println("✅ 사용자 정보 수정 완료: " + msg.getUserNo());
    }
}
