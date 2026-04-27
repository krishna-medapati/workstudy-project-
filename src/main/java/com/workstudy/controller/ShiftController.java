package com.workstudy.controller;

import com.workstudy.entity.Shift;
import com.workstudy.entity.User;
import com.workstudy.entity.Job;
import com.workstudy.repository.ShiftRepository;
import com.workstudy.repository.UserRepository;
import com.workstudy.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ShiftController(ShiftRepository shiftRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public List<Shift> getAll(Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() == User.Role.admin)
            return shiftRepository.findAll();
        return shiftRepository.findByStudentId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        try {
            Long studentId = Long.parseLong(body.get("student").toString());
            String date = body.getOrDefault("date", "").toString();
            String startTime = body.getOrDefault("startTime", "").toString();
            String endTime = body.getOrDefault("endTime", "").toString();

            // CONFLICT CHECK
            List<Shift> existing = shiftRepository.findByStudentId(studentId);
            for (Shift s : existing) {
                if (s.getDate().equals(date)) {
                    if (timesOverlap(s.getStartTime(), s.getEndTime(), startTime, endTime)) {
                        return ResponseEntity.badRequest().body(Map.of(
                            "msg", "Conflict! Student already has a shift from " + s.getStartTime() + " to " + s.getEndTime() + " on " + date
                        ));
                    }
                }
            }

            Shift shift = new Shift();
            User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
            Long jobId = Long.parseLong(body.get("job").toString());
            Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
            shift.setStudent(student);
            shift.setJob(job);
            shift.setDate(date);
            shift.setStartTime(startTime);
            shift.setEndTime(endTime);
            shift.setLocation(body.getOrDefault("location", "").toString());
            shift.setNotes(body.getOrDefault("notes", "").toString());
            shift.setStatus("scheduled");
            return ResponseEntity.ok(shiftRepository.save(shift));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("msg", e.getMessage()));
        }
    }

    private boolean timesOverlap(String start1, String end1, String start2, String end2) {
        return start1.compareTo(end2) < 0 && start2.compareTo(end1) < 0;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return shiftRepository.findById(id).map(shift -> {
            try {
                if (body.containsKey("student")) {
                    Long studentId = Long.parseLong(body.get("student").toString());
                    userRepository.findById(studentId).ifPresent(shift::setStudent);
                }
                if (body.containsKey("job")) {
                    Long jobId = Long.parseLong(body.get("job").toString());
                    jobRepository.findById(jobId).ifPresent(shift::setJob);
                }
                if (body.containsKey("date")) shift.setDate(body.get("date").toString());
                if (body.containsKey("startTime")) shift.setStartTime(body.get("startTime").toString());
                if (body.containsKey("endTime")) shift.setEndTime(body.get("endTime").toString());
                if (body.containsKey("location")) shift.setLocation(body.get("location").toString());
                if (body.containsKey("notes")) shift.setNotes(body.get("notes").toString());
                if (body.containsKey("status")) shift.setStatus(body.get("status").toString());
                return ResponseEntity.ok(shiftRepository.save(shift));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("msg", e.getMessage()));
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        shiftRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
