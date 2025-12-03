package com.example.minister_dashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "assistance_program")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "max_income_threshold")
    private BigDecimal maxIncomeThreshold;

    @Column(name = "payout_amount")
    private BigDecimal payoutAmount;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}
