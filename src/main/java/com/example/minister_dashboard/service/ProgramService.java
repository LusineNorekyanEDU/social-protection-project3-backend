package com.example.minister_dashboard.service;

import com.example.minister_dashboard.dto.ProgramDto;
import com.example.minister_dashboard.repository.AssistanceProgramRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    private final AssistanceProgramRepository repo;

    public ProgramService(AssistanceProgramRepository repo) {
        this.repo = repo;
    }

    //@Cacheable("programs")
    public List<ProgramDto> getAllPrograms(boolean onlyActive) {
        var programs = onlyActive
                ? repo.findByIsActiveTrueOrderByProgramNameAsc()
                : repo.findAll();

        return programs.stream()
                .map(p -> new ProgramDto(
                        p.getProgramId(),
                        p.getProgramName(),
                        p.getIsActive(),
                        p.getPayoutAmount()
                ))
                .toList();
    }
}
