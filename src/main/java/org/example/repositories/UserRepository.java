package org.example.repositories;


import org.example.databases.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Дополнительные методы поиска (если нужно)
    User findByEmail(String email);
}