package com.example.minister_dashboard.repository;

import com.example.minister_dashboard.model.AssistanceProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssistanceProgramRepository extends JpaRepository<AssistanceProgram, Long> {
    List<AssistanceProgram> findByIsActiveTrueOrderByProgramNameAsc();
}
