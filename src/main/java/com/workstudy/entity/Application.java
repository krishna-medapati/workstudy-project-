package com.workstudy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "student_id")
    private User student;
    @ManyToOne @JoinColumn(name = "job_id")
    private Job job;
    private String status;
    private LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() { appliedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }

    public static AppBuilder builder() { return new AppBuilder(); }
    public static class AppBuilder {
        private User student; private Job job; private String status;
        public AppBuilder student(User s) { this.student = s; return this; }
        public AppBuilder job(Job j) { this.job = j; return this; }
        public AppBuilder status(String s) { this.status = s; return this; }
        public Application build() {
            Application a = new Application();
            a.student = student; a.job = job; a.status = status;
            return a;
        }
    }
}
