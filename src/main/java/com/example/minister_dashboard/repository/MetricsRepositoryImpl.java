package com.example.minister_dashboard.repository;

import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.helper.BeneficiariesByCityRow;
import com.example.minister_dashboard.model.ApplicationStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MetricsRepositoryImpl implements MetricsRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ApplicationStatusCountRow> getApplicationFunnel(LocalDate from, LocalDate to, Long programId) {
        StringBuilder sql = new StringBuilder("""
    SELECT status, COUNT(*) AS cnt
    FROM v_application_summary
    WHERE status <> 'REVIEW'
    """);

        if (programId != null) {
            sql.append(" AND program_id = :programId");
        }
        if (from != null) {
            sql.append(" AND submission_date::date >= :fromDate");
        }
        if (to != null) {
            sql.append(" AND submission_date::date <= :toDate");
        }

        sql.append(" GROUP BY status");
        Query q = entityManager.createNativeQuery(sql.toString());
        if (programId != null) q.setParameter("programId", programId);
        if (from != null) q.setParameter("fromDate", from);
        if (to != null) q.setParameter("toDate", to);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<ApplicationStatusCountRow> result = new ArrayList<>();
        for (Object[] row : rows) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            ApplicationStatus status1 = ApplicationStatus.valueOf(status);
            result.add(new ApplicationStatusCountRow(status1, count));
        }

        return result;
    }

    @Override
    public List<BeneficiariesByCityRow> getBeneficiariesByCity(LocalDate from, LocalDate to, Long programId) {
        return List.of();
    }
}
