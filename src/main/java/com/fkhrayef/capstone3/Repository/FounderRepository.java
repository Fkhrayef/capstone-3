package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Founder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FounderRepository extends JpaRepository<Founder, Integer> {

    Founder findFounderById(Integer id);

    // Fetch a founder by startup id (first one)
    Founder findFirstByStartup_Id(Integer startupId);
}
