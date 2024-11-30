package org.example.controllers;

import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String registration(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model
    ) {
        boolean isRegistered = userService.registerUser(email, password);
        // Регистрируем нового пользователя
        if (isRegistered) {
            return "redirect:/login"; // Если регистрация успешна, перенаправляем на страницу логина
        } else {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String username,
            @RequestParam("password") String password,
            Model model
    ) {
        if (userService.login(username, password)) {
            // Если вход успешный, перенаправляем на главную страницу
            return "redirect:/dashboard";
        } else {
            // Если ошибка, возвращаемся на страницу входа с сообщением
            model.addAttribute("error", "Неверный email или пароль");
            return "login";
        }
    }
}
