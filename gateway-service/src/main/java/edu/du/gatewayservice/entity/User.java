package edu.du.gatewayservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_no;

    @Column(unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    private String post_url;

    @Column(nullable = false, unique = true)
    private String phone;
    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
