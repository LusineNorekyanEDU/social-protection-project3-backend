package com.example.minister_dashboard.service;

import com.example.minister_dashboard.dto.ApplicationFunnelDto;
import com.example.minister_dashboard.dto.BeneficiariesByCityDto;
import com.example.minister_dashboard.dto.FinancialLiabilityDto;
import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.helper.BeneficiariesByCityRow;
import com.example.minister_dashboard.helper.LiabilityRow;
import com.example.minister_dashboard.repository.MetricsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MetricsService {

    private final MetricsRepository metricsRepository;


    public MetricsService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    public ApplicationFunnelDto getApplicationFunnel(LocalDate from, LocalDate to, Long programId) {

        List<ApplicationStatusCountRow> rows = metricsRepository.getApplicationFunnel(from, to, programId);

        long total = rows.stream().mapToLong(ApplicationStatusCountRow::count).sum();

        List<ApplicationFunnelDto.Item> items = rows.stream()
                .map(r -> new ApplicationFunnelDto.Item(
                        r.applicationStatus().name(),
                        r.count(),
                        total == 0 ? 0.0 : ((double) (r.count() * 100) / total)
                )).toList();
        return new ApplicationFunnelDto(total, items);
    }

    public BeneficiariesByCityDto getBeneficiariesByCity(LocalDate from , LocalDate to ,Long programId) {
        List<BeneficiariesByCityRow> rows =  metricsRepository.getBeneficiariesByCity(from, to, programId);

        List<BeneficiariesByCityDto.Item> items = rows.stream()
                .map(r -> new BeneficiariesByCityDto.Item(r.city(),r.count()))
                .toList();
        return new BeneficiariesByCityDto(items);

    }

    public FinancialLiabilityDto getLiability(LocalDate from, LocalDate to, Long programId) {

        List<LiabilityRow> rows = metricsRepository.calculateLiability(from, to, programId);

        List<FinancialLiabilityDto.ProgramLiability> byProgram = rows.stream()
                .map(r -> {
                    BigDecimal projected = r.payoutAmount()
                            .multiply(BigDecimal.valueOf(r.approvedCount()));

                    return new FinancialLiabilityDto.ProgramLiability(
                            r.programId(),
                            r.programName(),
                            r.approvedCount(),
                            r.payoutAmount(),
                            projected
                    );
                })
                .toList();

        BigDecimal total = byProgram.stream()
                .map(FinancialLiabilityDto.ProgramLiability::projectedLiability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinancialLiabilityDto(total, byProgram);
    }
}
