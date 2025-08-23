package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Advisor;
import com.fkhrayef.capstone3.Model.AdvisorSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvisorSessionRepository extends JpaRepository<AdvisorSession, Integer> {
    List<AdvisorSession> findAdvisorSessionByStartupId(Integer startupId);

    AdvisorSession findAdvisorSessionByStartupIdAndStartDateAndNotes(Integer startupId, LocalDateTime startDate, String notes);

    AdvisorSession findAdvisorSessionById(Integer id);

    boolean existsByIdAndStatus(Integer id, String status);

    List<AdvisorSession> findAdvisorSessionsByAdvisorAndStatus(Advisor advisor, String status);

}
