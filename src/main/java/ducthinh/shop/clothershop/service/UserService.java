package ducthinh.shop.clothershop.service;

import ducthinh.shop.clothershop.entity.User;
import ducthinh.shop.clothershop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setRole("USER");
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        sendVerificationEmail(user);
    }

    private void sendVerificationEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Please verify your email");
        mailMessage.setText("Click on the link below to verify your email: "
                + "http://localhost:8080/verify?token=" + user.getVerificationToken());
        mailSender.send(mailMessage);
    }

    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setEnabled(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void createResetPasswordToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            sendResetPasswordEmail(user);
            logger.info("Reset password token created for user: {}", email);
        } else {
            logger.warn("Attempt to create reset password token for non-existent email: {}", email);
        }
    }

    public void sendResetPasswordEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Để đặt lại mật khẩu, vui lòng nhấp vào liên kết sau: " +
                "http://localhost:8080/reset-password?token=" + user.getResetPasswordToken());
        try {
            mailSender.send(message);
            logger.info("Reset password email sent to: {}", user.getEmail());
        } catch (MailException e) {
            logger.error("Failed to send reset password email to: {}", user.getEmail(), e);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user != null && user.getResetPasswordTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
