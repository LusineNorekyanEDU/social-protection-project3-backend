package com.example.minister_dashboard.helper;

import java.math.BigDecimal;

public record LiabilityRow(Long programId,
                           String programName,
                           BigDecimal payoutAmount,
                           long approvedCount) {
}
