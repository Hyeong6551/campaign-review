package edu.du.gatewayservice.config;

import edu.du.gatewayservice.entity.Role;
import edu.du.gatewayservice.entity.User;
import edu.du.gatewayservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        // 테스트 사용자 생성
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setId("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(Role.ADMIN);
            admin.setNickname("admin");
            admin.setPhone("01012345678");
            userRepository.save(admin);

            User user1 = new User();
            user1.setId("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setEmail("user1@example.com");
            user1.setRole(Role.USER);
            user1.setNickname("user1");
            user1.setPhone("01011112222");
            userRepository.save(user1);

            User user2 = new User();
            user2.setId("user2");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.setEmail("user2@example.com");
            user2.setRole(Role.ADMIN);
            user2.setNickname("user2");
            user2.setPhone("01011111111");
            userRepository.save(user2);
        }
    }
} 