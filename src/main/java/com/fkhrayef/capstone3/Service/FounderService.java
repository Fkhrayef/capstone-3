package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.FounderDTO;
import com.fkhrayef.capstone3.Model.Founder;
import com.fkhrayef.capstone3.Repository.FounderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FounderService {

    private final FounderRepository founderRepository;

    public List<Founder> getAllFounders() {
        return founderRepository.findAll();
    }

    public void addFounder(FounderDTO founderDTO) {
        Founder founder = new Founder();
        founder.setName(founderDTO.getName());
        founder.setEmail(founderDTO.getEmail());
        founder.setPhone(founderDTO.getPhone());
        founder.setEquityPercentage(0.0);
        founder.setHasStartup(false);

        founderRepository.save(founder);
    }

    public void updateFounder(Integer id, FounderDTO founderDTO) {
        Founder founder = founderRepository.findFounderById(id);
        if (founder == null) {
            throw new ApiException("Founder not found with id: " + id);
        }

        founder.setName(founderDTO.getName());
        founder.setEmail(founderDTO.getEmail());
        founder.setPhone(founderDTO.getPhone());

        founderRepository.save(founder);
    }

    public void deleteFounder(Integer id) {
        Founder founder = founderRepository.findFounderById(id);
        if (founder == null) {
            throw new ApiException("Founder not found with id: " + id);
        }
        if (founder.getHasStartup() && founder.getStartup().getFounders().size() == 1) {
            throw new ApiException("Founder is the only founder at his startup, try deleting the startup first!");
        }
        if (founder.getHasStartup()) {
            // JPA will handle relationship cleanup automatically
            founder.setHasStartup(false);
            founder.setStartup(null);
            founder.setEquityPercentage(0.0);
        }
        founderRepository.delete(founder);
    }

}
