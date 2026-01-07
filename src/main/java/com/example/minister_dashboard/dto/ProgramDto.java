package com.example.minister_dashboard.dto;

import java.math.BigDecimal;

public record ProgramDto (Long programId,
                          String programName,
                          Boolean isActive,
                          BigDecimal payoutAmount){
}
