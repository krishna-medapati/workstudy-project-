package com.workstudy.repository;

import com.workstudy.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByStudentId(Long studentId);
}
