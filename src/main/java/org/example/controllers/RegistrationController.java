package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Map;

@RestController
@RequestMapping("/api") // чтобы все запросы шли с префиксом /api, можно убрать, если не нужно
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody Map<String, String> userData // принимаем JSON с email и password
    ) {
        String email = userData.get("email");
        String password = userData.get("password");

        boolean isRegistered = userService.registerUser(email, password);

        if (isRegistered) {
            // Вернём статус 200 и сообщение об успехе
            return ResponseEntity.ok(Map.of("message", "Регистрация успешна"));
        } else {
            // Вернём ошибку 400 с сообщением
            return ResponseEntity.badRequest().body(Map.of("message", "Пользователь с таким email уже существует"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody Map<String, String> userData
    ) {
        String email = userData.get("username"); // или "email" — смотри, что клиент посылает
        String password = userData.get("password");

        if (userService.login(email, password)) {
            // Можно вернуть токен, либо просто сообщение
            return ResponseEntity.ok(Map.of("message", "Вход успешен"));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Неверный email или пароль"));
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("username", authentication.getName()));
        } else {
            return ResponseEntity.status(401).build();
        }
    }
    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
