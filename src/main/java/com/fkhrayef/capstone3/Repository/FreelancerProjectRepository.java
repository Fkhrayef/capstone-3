package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.FreelancerProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerProjectRepository extends JpaRepository<FreelancerProject, Integer> {

    @Query("select p from FreelancerProject p where p.startup.id=:startupId")
    List<FreelancerProject> giveMeFreelancerProjectByStartupId(Integer startupId);

    FreelancerProject findFreelancerProjectByProjectNameAndDescriptionAndStartupId(String projectName, String description, Integer startupId);

    FreelancerProject findFreelancerProjectById(Integer id);

    @Query("select p from FreelancerProject  p where p.freelancer.id=:freelancerId and p.status=:status")
    List<FreelancerProject> giveMeFreelancerProjectByFreelancerIdAndStatus(Integer freelancerId, String status);
}
