package com.mo.volunteer.repo;

import com.mo.volunteer.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Query(value = """
        SELECT
          v.student_id   AS studentId,
          MIN(v.first_name) AS firstName,
          MIN(v.last_name)  AS lastName,
          MIN(v.email)      AS email,
          COALESCE(SUM(TIMESTAMPDIFF(MINUTE, v.time_in, v.time_out)), 0) AS totalMinutes
        FROM volunteers v
        WHERE v.time_in  IS NOT NULL
          AND v.time_out IS NOT NULL
          AND v.time_out >= v.time_in
          AND v.time_in >= :start
          AND v.time_in <  :end
        GROUP BY v.student_id
        HAVING COALESCE(SUM(TIMESTAMPDIFF(MINUTE, v.time_in, v.time_out)), 0) > :minMinutes
        ORDER BY totalMinutes DESC
        """, nativeQuery = true)
    List<AwardRow> awardRowsForYear(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("minMinutes") long minMinutes);
}
