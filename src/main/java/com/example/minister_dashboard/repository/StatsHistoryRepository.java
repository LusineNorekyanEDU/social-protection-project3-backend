package com.example.minister_dashboard.repository;

import com.example.minister_dashboard.model.StatsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;



public interface StatsHistoryRepository extends JpaRepository<StatsHistory,Long> {
    void deleteByStatDateAndProgramId(LocalDate statDate, Long programId);

    default void saveOrReplace(StatsHistory stats) {
        deleteByStatDateAndProgramId(stats.getStatDate(), stats.getProgramId());
        save(stats);
    }
}
