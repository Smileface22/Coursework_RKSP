package org.example.databases;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private User user;  // Объект пользователя

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;  // Роли не используются, можно вернуть null
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // Возвращаем пароль пользователя
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // Используем email как имя пользователя
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Учетная запись не истекла
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Учетная запись не заблокирована
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Учетные данные не истекли
    }

    @Override
    public boolean isEnabled() {
        return true;  // Учетная запись активна
    }
}
