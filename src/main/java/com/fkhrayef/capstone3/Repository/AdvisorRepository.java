package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvisorRepository extends JpaRepository<Advisor, Integer> {
    Advisor findAdvisorById(Integer id);
}
