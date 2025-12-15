package com.example.minister_dashboard.controller;

import com.example.minister_dashboard.dto.ApplicationFunnelDto;
import com.example.minister_dashboard.dto.BeneficiariesByCityDto;
import com.example.minister_dashboard.dto.FinancialLiabilityDto;
import com.example.minister_dashboard.service.MetricsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "http://localhost:4200")
public class MetricsController {
    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

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

}
