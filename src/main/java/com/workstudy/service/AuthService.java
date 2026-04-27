package com.workstudy.service;

import com.workstudy.entity.User;
import com.workstudy.repository.UserRepository;
import com.workstudy.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> register(String name, String email, String password, String role) {
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email already exists");
        User user = User.builder()
                .name(name).email(email)
                .password(passwordEncoder.encode(password))
                .role(User.Role.valueOf(role))
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(email, role);
        return Map.of("token", token, "user", Map.of("id", user.getId(), "name", name, "email", email, "role", role));
    }

    public void validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (password != null && !passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Invalid password");
    }

    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (password != null && !passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Invalid password");
        String token = jwtUtil.generateToken(email, user.getRole().name());
        return Map.of("token", token, "user", Map.of("id", user.getId(), "name", user.getName(), "email", email, "role", user.getRole().name()));
    }
}
