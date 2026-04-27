package com.workstudy.controller;

import com.workstudy.entity.Job;
import com.workstudy.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobRepository jobRepository;
    public JobController(JobRepository jobRepository) { this.jobRepository = jobRepository; }

    @GetMapping
    public List<Job> getAll() { return jobRepository.findAll(); }

    @PostMapping
    public Job create(@RequestBody Job job) { return jobRepository.save(job); }

    @PutMapping("/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id, @RequestBody Job job) {
        return jobRepository.findById(id).map(j -> {
            job.setId(id);
            return ResponseEntity.ok(jobRepository.save(job));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        jobRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
