package com.example.minister_dashboard.controller;

import com.example.minister_dashboard.dto.ApplicationFunnelDto;
import com.example.minister_dashboard.dto.BeneficiariesByCityDto;
import com.example.minister_dashboard.dto.FinancialLiabilityDto;
import com.example.minister_dashboard.dto.ProgramDto;
import com.example.minister_dashboard.service.MetricsService;
import com.example.minister_dashboard.service.ProgramService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/metrics")
public class MetricsController {
    private final MetricsService metricsService;
    private final ProgramService programService;

    public MetricsController(MetricsService metricsService, ProgramService programService) {
        this.metricsService = metricsService;
        this.programService = programService;
    }

    @PreAuthorize("hasAuthority('ANALYTICS:VIEW')")
    @GetMapping("/application-funnel")
    public ApplicationFunnelDto getFunnel(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long programId
    ) {
        return metricsService.getApplicationFunnel(from, to, programId);
    }

    @PreAuthorize("hasAuthority('ANALYTICS:VIEW')")
    @GetMapping("/beneficiaries-by-city")
    public BeneficiariesByCityDto getBeneficiaries(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long programId
    ) {
        return metricsService.getBeneficiariesByCity(from, to, programId);
    }

    @PreAuthorize("hasAuthority('ANALYTICS:VIEW')")
    @GetMapping(
            value = "/beneficiaries-by-city/csv",
            produces = "text/csv"
    )
    public ResponseEntity<byte[]> downloadBeneficiariesCsv(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long programId
    ){
        BeneficiariesByCityDto dto = metricsService.getBeneficiariesByCity(from, to, programId);

        StringBuilder sb = new StringBuilder();
        sb.append("city,beneficiariesCount\n");

        dto.getItems().forEach(item -> {
            sb.append(item.getCity() == null ? "" : item.getCity())
                    .append(",")
                    .append(item.getBeneficiaryCount())
                    .append("\n");
        });

        byte[] csv = sb.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=beneficiaries_by_city.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }

    @PreAuthorize("hasAuthority('ANALYTICS:VIEW')")
    @GetMapping("/financial-liability")
    public FinancialLiabilityDto getLiability(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long programId
    ) {
        return metricsService.getLiability(from, to, programId);
    }

    @PreAuthorize("hasAuthority('ANALYTICS:VIEW')")
    @GetMapping("/programs")
    public List<ProgramDto> getPrograms(
            @RequestParam(defaultValue = "true") boolean onlyActive
    ) {
        return programService.getAllPrograms(onlyActive);
    }
}
