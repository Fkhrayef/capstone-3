package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface FreelancerRepository extends JpaRepository<Freelancer, Integer> {
    Freelancer findFreelancerById(Integer id);
}
