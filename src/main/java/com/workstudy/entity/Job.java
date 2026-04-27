package com.workstudy.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title, department, description, status, location, salary;
    private Integer slots;

    @ElementCollection
    @CollectionTable(name = "job_time_slots", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "time_slot")
    private List<String> timeSlots;

    @ElementCollection
    @CollectionTable(name = "job_work_days", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "work_day")
    private List<String> workDays;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getDepartment() { return department; }
    public void setDepartment(String d) { this.department = d; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public String getSalary() { return salary; }
    public void setSalary(String s) { this.salary = s; }
    public Integer getSlots() { return slots; }
    public void setSlots(Integer s) { this.slots = s; }
    public List<String> getTimeSlots() { return timeSlots; }
    public void setTimeSlots(List<String> t) { this.timeSlots = t; }
    public List<String> getWorkDays() { return workDays; }
    public void setWorkDays(List<String> w) { this.workDays = w; }
}
