package com.example.minister_dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record FinancialLiabilityDto(BigDecimal totalLiability,
                                    List<ProgramLiability> byProgram) {
    public record ProgramLiability(
            Long programId,
            String programName,
            long approvedCount,
            BigDecimal payoutAmount,
            BigDecimal projectedLiability
    ) {}
}
