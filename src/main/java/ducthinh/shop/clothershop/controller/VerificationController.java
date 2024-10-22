package ducthinh.shop.clothershop.controller;

import ducthinh.shop.clothershop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {
    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token, Model model) {
        boolean isVerified = userService.verifyUser(token);
        model.addAttribute("isVerified", isVerified);
        return "verification";
    }
}
