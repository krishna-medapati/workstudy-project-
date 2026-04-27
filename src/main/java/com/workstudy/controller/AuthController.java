package com.workstudy.controller;

import com.workstudy.service.AuthService;
import com.workstudy.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(authService.register(body.get("name"), body.get("email"), body.get("password"), body.getOrDefault("role", "student")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("msg", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            authService.validateCredentials(body.get("email"), body.get("password"));
            otpService.generateAndSendOtp(body.get("email"));
            return ResponseEntity.ok(Map.of("msg", "OTP sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("msg", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        try {
            boolean valid = otpService.verifyOtp(body.get("email"), body.get("otp"));
            if (!valid) return ResponseEntity.badRequest().body(Map.of("msg", "Invalid or expired OTP"));
            return ResponseEntity.ok(authService.login(body.get("email"), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("msg", e.getMessage()));
        }
    }
}
