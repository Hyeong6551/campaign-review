package edu.du.gatewayservice.service;

import edu.du.gatewayservice.dto.RegisterRequest;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
