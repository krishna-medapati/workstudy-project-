package com.workstudy.controller;

import com.workstudy.entity.User;
import com.workstudy.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final UserRepository userRepository;

    public StudentController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.student)
                .toList();
    }
}
