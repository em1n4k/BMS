package com.bankcards.service;

import com.bankcards.dto.AuthResponse;
import com.bankcards.entity.Role;
import com.bankcards.repository.UserRepository;
import com.bankcards.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankcards.security.JwtTokenProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean AuthFlag = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!AuthFlag) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        Map<String, Object> claims = Map.of("roles", roles);

        String token = jwtTokenProvider.createToken(user.getUsername(), roles);

        return new AuthResponse(token, roles);
    }
}
