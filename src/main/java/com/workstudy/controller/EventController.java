package com.workstudy.controller;
import com.workstudy.model.Event;
import com.workstudy.model.EventRegistration;
import com.workstudy.repository.EventRepository;
import com.workstudy.repository.EventRegistrationRepository;
import com.workstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired EventRepository eventRepo;
    @Autowired EventRegistrationRepository regRepo;
    @Autowired UserRepository userRepo;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepo.save(event));
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventRepo.findAll());
    }

    @PostMapping("/{eventId}/register/{userId}")
    public ResponseEntity<?> register(@PathVariable Long eventId, @PathVariable Long userId) {
        if (regRepo.existsByEventIdAndUserId(eventId, userId)) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Already registered"));
        }
        EventRegistration reg = new EventRegistration();
        reg.setEventId(eventId);
        reg.setUserId(userId);
        return ResponseEntity.ok(regRepo.save(reg));
    }

    @GetMapping("/{eventId}/registrations")
    public ResponseEntity<?> getRegistrations(@PathVariable Long eventId) {
        List<EventRegistration> regs = regRepo.findByEventId(eventId);
        return ResponseEntity.ok(regs);
    }
}
