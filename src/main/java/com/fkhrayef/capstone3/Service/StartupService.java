package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.StartupDTO;
import com.fkhrayef.capstone3.Model.Founder;
import com.fkhrayef.capstone3.Model.Startup;
import com.fkhrayef.capstone3.Repository.FounderRepository;
import com.fkhrayef.capstone3.Repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final StartupRepository startupRepository;
    private final FounderRepository founderRepository;

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    public void addStartup(Integer founderId, StartupDTO startupDTO) {
        Founder founder = founderRepository.findFounderById(founderId);
        if (founder == null) {
            throw new ApiException("Founder not found with id: " + founderId);
        }

        if (founder.getHasStartup()) {
            throw new ApiException("Founder already has a startup");
        }

        Startup startup = new Startup();
        startup.setName(startupDTO.getName());
        startup.setDescription(startupDTO.getDescription());
        startup.setIndustry(startupDTO.getIndustry());
        startup.setStage(startupDTO.getStage());
        startup.setFoundedDate(startupDTO.getFoundedDate());
        startup.setEmployeeCount(startupDTO.getEmployeeCount());
        startup.setValuation(startupDTO.getValuation());
        startup.setStatus("active");

        startup.setDailyAiUsageCount(0);
        startup.setDailyAiLimit(10);

        startupRepository.save(startup);

        // Link founder to startup
        founder.setHasStartup(true);
        founder.setEquityPercentage(100.0);
        founder.setStartup(startup);
        founderRepository.save(founder);
    }

    public void addFounderToStartup(Integer startupId, Integer founderId, Double equityPercentage) {
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("Startup not found with id: " + startupId);
        }

        Founder founder = founderRepository.findFounderById(founderId);
        if (founder == null) {
            throw new ApiException("Founder not found with id: " + founderId);
        }

        if (founder.getHasStartup()) {
            throw new ApiException("Founder already has a startup");
        }

        if (equityPercentage <= 0 || equityPercentage > 100) {
            throw new ApiException("Equity percentage must be between 0 and 100");
        }

        // Get current total equity (should always be 100% for existing startups)
        double currentTotalEquity = startup.getFounders().stream()
                .mapToDouble(Founder::getEquityPercentage)
                .sum();

        // ALWAYS redistribute equity proportionally to maintain 100% total
        double redistributionRatio = (100.0 - equityPercentage) / currentTotalEquity;

        // Update existing founders' equity proportionally
        for (Founder existingFounder : startup.getFounders()) {
            double newEquity = existingFounder.getEquityPercentage() * redistributionRatio;
            existingFounder.setEquityPercentage(newEquity);
            founderRepository.save(existingFounder);
        }

        founder.setHasStartup(true);
        founder.setEquityPercentage(equityPercentage);
        founder.setStartup(startup);
        founderRepository.save(founder);
    }

    public void updateStartup(Integer id, StartupDTO startupDTO) {
        Startup startup = startupRepository.findStartupById(id);
        if (startup == null) {
            throw new ApiException("Startup not found with id: " + id);
        }

        startup.setName(startupDTO.getName());
        startup.setDescription(startupDTO.getDescription());
        startup.setIndustry(startupDTO.getIndustry());
        startup.setStage(startupDTO.getStage());
        startup.setFoundedDate(startupDTO.getFoundedDate());
        startup.setEmployeeCount(startupDTO.getEmployeeCount());
        startup.setValuation(startupDTO.getValuation());

        startupRepository.save(startup);
    }

    public void deleteStartup(Integer id) {
        Startup startup = startupRepository.findStartupById(id);
        if (startup == null) {
            throw new ApiException("Startup not found with id: " + id);
        }
        // Clean up founder relationships
        for (Founder founder : startup.getFounders()) {
            founder.setHasStartup(false);
            founder.setStartup(null);
            founder.setEquityPercentage(0.0);
            founderRepository.save(founder);
        }

        startupRepository.delete(startup);
    }
}
