package ducthinh.shop.clothershop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "role")
    private String role = "USER";

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private boolean enabled;

    @Column(name = "verification_token", unique = true)
    private String verificationToken;

    @Column(unique = true)
    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;
}