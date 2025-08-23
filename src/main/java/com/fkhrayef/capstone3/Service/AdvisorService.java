package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.AdvisorDTO;
import com.fkhrayef.capstone3.Model.Advisor;
import com.fkhrayef.capstone3.Repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdvisorService {

    private final AdvisorRepository advisorRepository;

    public List<Advisor> getAllAdvisors() {
        return advisorRepository.findAll();
    }

    public void addAdvisor(AdvisorDTO advisorDTO) {

        //sets the DTO values + the isAvailable to true and advisorSessions to null
        Advisor advisor = new Advisor();
        advisor.setName(advisorDTO.getName());
        advisor.setEmail(advisorDTO.getEmail());
        advisor.setPhone(advisorDTO.getPhone());
        advisor.setExpertiseArea(advisorDTO.getExpertiseArea());
        advisor.setYearsExperience(advisorDTO.getYearsExperience());
        advisor.setHourlyRate(advisorDTO.getHourlyRate());
        advisor.setIsAvailable(true);

        // Set default value for earnings
        advisor.setTotalEarnings(0.0);

        advisorRepository.save(advisor);
    }

    public void updateAdvisor(Integer id, AdvisorDTO advisorDTO) {
        Advisor oldAdvisor = advisorRepository.findAdvisorById(id);
        if (oldAdvisor == null) {
            throw new ApiException("Advisor not found");
        }
        oldAdvisor.setName(advisorDTO.getName());
        oldAdvisor.setEmail(advisorDTO.getEmail());
        oldAdvisor.setPhone(advisorDTO.getPhone());
        oldAdvisor.setExpertiseArea(advisorDTO.getExpertiseArea());
        oldAdvisor.setYearsExperience(advisorDTO.getYearsExperience());
        oldAdvisor.setHourlyRate(advisorDTO.getHourlyRate());
        advisorRepository.save(oldAdvisor);
    }

    public void deleteAdvisor(Integer id) {
        Advisor oldAdvisor = advisorRepository.findAdvisorById(id);
        if (oldAdvisor == null) {
            throw new ApiException("Advisor not found");
        }
        advisorRepository.delete(oldAdvisor);
    }


}
