package com.example.minister_dashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "stats_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"stat_date", "program_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "program_name")
    private String programName;

    private long submittedCount;
    private long approvedCount;
    private long rejectedCount;

    private long beneficiariesCount;

    private BigDecimal totalLiability;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public static StatsHistory overall(
            LocalDate date,
            long submitted,
            long approved,
            long rejected,
            long beneficiaries,
            BigDecimal liability
    ) {
        return new StatsHistory(
                null, date, null, null,
                submitted, approved, rejected,
                beneficiaries, liability, OffsetDateTime.now()
        );
    }

    public static StatsHistory perProgram(
            LocalDate date,
            Long programId,
            String programName,
            long approved,
            long beneficiaries,
            BigDecimal liability
    ) {
        return new StatsHistory(
                null, date, programId, programName,
                0, approved, 0,
                beneficiaries, liability, OffsetDateTime.now()
        );
    }
}
