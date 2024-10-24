package ducthinh.shop.clothershop.controller;


import ducthinh.shop.clothershop.entity.User;
import ducthinh.shop.clothershop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

//    @GetMapping("/verify")
//    @RequestMapping(params = "!email") // Chỉ xử lý khi request không có tham số "email"
//    public String verifyUser(@RequestParam String token) {
//        if (userService.verifyUser(token)) {
//            return "redirect:/login?verified=true";
//        }
//        return "redirect:/login?verified=false";
//    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";

    }
}
