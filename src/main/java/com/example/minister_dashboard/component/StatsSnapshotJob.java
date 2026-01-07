package com.example.minister_dashboard.component;

import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.helper.LiabilityRow;
import com.example.minister_dashboard.model.StatsHistory;
import com.example.minister_dashboard.repository.MetricsRepository;
import com.example.minister_dashboard.repository.StatsHistoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsSnapshotJob {

    private final MetricsRepository metricsRepository;
    private final StatsHistoryRepository statsHistoryRepository;

    public StatsSnapshotJob(MetricsRepository metricsRepository,
                            StatsHistoryRepository statsHistoryRepository) {
        this.metricsRepository = metricsRepository;
        this.statsHistoryRepository = statsHistoryRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Yerevan")
    @Transactional
    public void snapshotYesterday() {

        LocalDate day = LocalDate.now().minusDays(1);

        // Widget 1: Funnel counts

        List<ApplicationStatusCountRow> funnelRows =
                metricsRepository.getApplicationFunnel(day, day, null);

        Map<String, Long> counts = funnelRows.stream()
                .collect(Collectors.toMap(
                        r -> r.applicationStatus().name(),
                        ApplicationStatusCountRow::count
                ));

        long submitted = counts.getOrDefault("SUBMITTED", 0L);
        long approved = counts.getOrDefault("APPROVED", 0L);
        long rejected = counts.getOrDefault("REJECTED", 0L);

        // Widget 2: Beneficiaries

        long beneficiaries = metricsRepository.countApprovedBeneficiaries(day, day, null);

        // Widget 3: Liability

        List<LiabilityRow> liabilityRows =
                metricsRepository.calculateLiability(day, day, null);

        BigDecimal totalLiability = liabilityRows.stream()
                .map(r -> r.payoutAmount().multiply(BigDecimal.valueOf(r.approvedCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        StatsHistory overall = new StatsHistory(
                null,
                day,
                null,
                null,
                submitted,
                approved,
                rejected,
                beneficiaries,
                totalLiability,
                OffsetDateTime.now()
        );

        statsHistoryRepository.saveOrReplace(overall);

    }

}
