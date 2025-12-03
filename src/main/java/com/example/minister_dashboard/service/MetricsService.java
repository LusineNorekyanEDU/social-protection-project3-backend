package com.example.minister_dashboard.service;

import com.example.minister_dashboard.dto.ApplicationFunnelDto;
import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.repository.MetricsRepository;
import org.springframework.stereotype.Service;

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
}
