package com.example.minister_dashboard.repository;

import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.helper.BeneficiariesByCityRow;

import java.time.LocalDate;
import java.util.List;

public interface MetricsRepository {
    List<ApplicationStatusCountRow> getApplicationFunnel(LocalDate from, LocalDate to, Long programId);

    List<BeneficiariesByCityRow> getBeneficiariesByCity(LocalDate from, LocalDate to, Long programId);
}
