package org.example.services;


import org.example.databases.User;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Метод для регистрации нового пользователя
    public boolean  registerUser(String email, String password) {
        // Хэшируем пароль перед сохранением
        String hashedPassword = passwordEncoder.encode(password);
        if (userRepository.findByEmail(email) != null) {
            return false; // Регистрация невозможна, email уже существует
        }
        // Создаём объект пользователя и сохраняем в базе данных
        User user = new User(email, hashedPassword);
        userRepository.save(user);
        return true;
    }

    // Метод для проверки пользователя
    public boolean login(String email, String password) {
        // Находим пользователя по email
        User user = userRepository.findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Используем email для поиска пользователя в базе данных
        return userRepository.findByEmail(userDetails.getUsername()); // findByEmail должен быть в репозитории
    }
}
