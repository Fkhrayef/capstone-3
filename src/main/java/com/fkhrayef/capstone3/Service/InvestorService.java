package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.Model.Investor;
import com.fkhrayef.capstone3.Repository.InvestorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestorService {
    private final InvestorRepository investorRepository;

    // get method:
    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    // post method:
    public void addInvestor(Investor investor) {
        investorRepository.save(investor);
    }

    // update method:
    public void updateInvestor(Integer id, Investor investor) {

        // 1- check if investor exist:
        Investor oldInvestor = investorRepository.findInvestorById(id);
        if (oldInvestor == null) {
            throw new ApiException("investor not found");
        }

        // 2- apply changes:
        oldInvestor.setName(investor.getName());
        oldInvestor.setEmail(investor.getEmail());
        oldInvestor.setOrganizationName(investor.getOrganizationName());
        oldInvestor.setInvestmentFocus(investor.getInvestmentFocus());

        // 3- save to repo:
        investorRepository.save(oldInvestor);
    }

    public void deleteInvestor(Integer id) {

        // 1- check if investor exist:
        Investor investor = investorRepository.findInvestorById(id);
        if (investor == null) {
            throw new ApiException("investor not found");
        }

        investorRepository.delete(investor);

    }
}
