package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartupRepository extends JpaRepository<Startup, Integer> {

    Startup findStartupById(Integer id);
}
