package com.workstudy.controller;

import com.workstudy.entity.Application;
import com.workstudy.entity.Job;
import com.workstudy.entity.User;
import com.workstudy.repository.ApplicationRepository;
import com.workstudy.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationController(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public List<Application> getAll(Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() == User.Role.admin)
            return applicationRepository.findAll();
        return applicationRepository.findByStudentId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody Map<String, Long> body, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Job job = jobRepository.findById(body.get("jobId")).orElseThrow();
        boolean alreadyApplied = applicationRepository.findByStudentId(user.getId())
                .stream().anyMatch(a -> a.getJob().getId().equals(job.getId()));
        if (alreadyApplied) {
            return ResponseEntity.badRequest().body(Map.of("msg", "You have already applied for this job"));
        }
        Application app = Application.builder()
                .student(user).job(job).status("pending").build();
        return ResponseEntity.ok(applicationRepository.save(app));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return applicationRepository.findById(id).map(app -> {
            app.setStatus(body.get("status"));
            return ResponseEntity.ok(applicationRepository.save(app));
        }).orElse(ResponseEntity.notFound().build());
    }
}
