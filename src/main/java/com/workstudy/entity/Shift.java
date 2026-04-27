package com.workstudy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "student_id")
    private User student;
    @ManyToOne @JoinColumn(name = "job_id")
    private Job job;
    private String date, startTime, endTime, location, notes, status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getStudent() { return student; }
    public void setStudent(User s) { this.student = s; }
    public Job getJob() { return job; }
    public void setJob(Job j) { this.job = j; }
    public String getDate() { return date; }
    public void setDate(String d) { this.date = d; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String s) { this.startTime = s; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String e) { this.endTime = e; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}
