package ducthinh.shop.clothershop.controller;

import ducthinh.shop.clothershop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            userService.createResetPasswordToken(email);
            logger.info("Reset password token created for email: {}", email);
            redirectAttributes.addFlashAttribute("message", "Nếu email tồn tại, bạn sẽ nhận được hướng dẫn đặt lại mật khẩu.");
            return "redirect:/forgot-password?success";
        } catch (Exception e) {
            logger.error("Error creating reset password token for email: {}", email, e);
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            return "redirect:/forgot-password?error";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password) {
        if (userService.resetPassword(token, password)) {
            return "redirect:/login?resetSuccess";
        } else {
            return "redirect:/reset-password?token=" + token + "&error";
        }
    }
}
