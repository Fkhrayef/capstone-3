package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {


    Investment findInvestmentById(Integer id);
}
