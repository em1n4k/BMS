package com.bankcards.service;

import com.bankcards.dto.AuthResponse;
import com.bankcards.entity.Role;
import com.bankcards.repository.UserRepository;
import com.bankcards.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean AuthFlag = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!AuthFlag) {
            throw new RuntimeException("Invalid credentials");
        }

        String temporaryToken = "TEMP_TOKEN";

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new AuthResponse(temporaryToken, roles);
    }
}
