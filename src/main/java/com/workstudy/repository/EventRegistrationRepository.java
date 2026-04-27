package com.workstudy.repository;
import com.workstudy.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByEventId(Long eventId);
    List<EventRegistration> findByUserId(Long userId);
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
