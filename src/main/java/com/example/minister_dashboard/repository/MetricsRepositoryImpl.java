package com.example.minister_dashboard.repository;

import com.example.minister_dashboard.helper.ApplicationStatusCountRow;
import com.example.minister_dashboard.helper.BeneficiariesByCityRow;
import com.example.minister_dashboard.helper.LiabilityRow;
import com.example.minister_dashboard.model.ApplicationStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
        StringBuilder sql = new StringBuilder("""
            SELECT city, COUNT(DISTINCT citizen_id) AS cnt
            FROM v_application_summary
            WHERE status = 'APPROVED'
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
        sql.append(" GROUP BY city");
        sql.append(" ORDER BY cnt DESC");

        Query query = entityManager.createNativeQuery(sql.toString());

        if (programId != null) query.setParameter("programId", programId);
        if (from != null) query.setParameter("fromDate", from);
        if (to != null) query.setParameter("toDate", to);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<BeneficiariesByCityRow> result = new ArrayList<>();
        for (Object[] r : rows) {
            String city = (String) r[0];
            long count = ((Number) r[1]).longValue();
            result.add(new BeneficiariesByCityRow(city, count));
        }
        return result;
    }

    @Override
    public List<LiabilityRow> calculateLiability(LocalDate from, LocalDate to, Long programId) {

        String sql = """
            SELECT
              program_id,
              program_name,
              MAX(payout_amount) AS payout_amount,
              COUNT(*) AS approved_count
            FROM v_application_summary
            WHERE status = 'APPROVED'
              AND (CAST(:programId AS BIGINT) IS NULL OR program_id = :programId)
              AND (CAST(:fromDate AS DATE) IS NULL OR submission_date::date >= :fromDate)
              AND (CAST(:toDate   AS DATE) IS NULL OR submission_date::date <= :toDate)
            GROUP BY program_id, program_name
            ORDER BY program_name
            """;

        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("programId", programId);
        q.setParameter("fromDate", from);
        q.setParameter("toDate", to);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<LiabilityRow> result = new ArrayList<>();

        for (Object[] r : rows) {
            Long pid = ((Number) r[0]).longValue();
            String pname = (String) r[1];
            BigDecimal payout = (BigDecimal) r[2];
            long approvedCount = ((Number) r[3]).longValue();

            result.add(new LiabilityRow(pid, pname, payout, approvedCount));
        }

        return result;
    }

    @Override
    public long countApprovedBeneficiaries(LocalDate from, LocalDate to, Long programId) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(DISTINCT citizen_id) AS cnt
        FROM v_application_summary
        WHERE status = 'APPROVED'
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

        Query q = entityManager.createNativeQuery(sql.toString());

        if (programId != null) q.setParameter("programId", programId);
        if (from != null) q.setParameter("fromDate", from);
        if (to != null) q.setParameter("toDate", to);

        return ((Number) q.getSingleResult()).longValue();
    }

}
