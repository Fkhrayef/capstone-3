package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.InvestmentDTO;
import com.fkhrayef.capstone3.DTOout.ContractDTO;
import com.fkhrayef.capstone3.Model.Investment;
import com.fkhrayef.capstone3.Model.Investor;
import com.fkhrayef.capstone3.Model.Startup;
import com.fkhrayef.capstone3.Repository.InvestmentRepository;
import com.fkhrayef.capstone3.Repository.InvestorRepository;
import com.fkhrayef.capstone3.Repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestorRepository investorRepository;
    private final StartupRepository startupRepository;
    private final FileService fileService;
    private final S3Service s3Service;

    public List<Investment> getAllInvestments() {
        return investmentRepository.findAll();
    }

    public void createInvestment(InvestmentDTO investmentDTO, String exchange) throws ApiException {
        Investor investor = investorRepository.findInvestorById(investmentDTO.getInvestor_id());
        if (investor == null) {
            throw new ApiException("Investor not found");
        }
        Startup startup = startupRepository.findStartupById(investmentDTO.getStartup_id());
        if (startup == null) {
            throw new ApiException("Startup not found");
        }

        if (investmentDTO.getIsRecurring()) {
            if (investmentDTO.getRecurringAmount() == null || investmentDTO.getRecurringYears() == null) {
                throw new ApiException("Recurring amount and years must be provided");
            }
            Investment investment = new Investment(null, investmentDTO.getRoundType(), investmentDTO.getEffectiveDate(), investmentDTO.getInvestment_amount(), investmentDTO.getPaymentMethod(), true, investmentDTO.getRecurringAmount(), investmentDTO.getRecurringYears(), investmentDTO.getMinimumInvestmentPeriod(), investor, startup, null);
            investmentRepository.save(investment);
            createContract(investment, exchange);
        } else {
            if (investmentDTO.getRecurringAmount() != null || investmentDTO.getRecurringYears() != null) {
                throw new ApiException("Recurring amount and years can not be set for non-recurring investment.");
            }
            Investment investment = new Investment(null, investmentDTO.getRoundType(), investmentDTO.getEffectiveDate(), investmentDTO.getInvestment_amount(), investmentDTO.getPaymentMethod(), false, null, null, investmentDTO.getMinimumInvestmentPeriod(), investor, startup, null);
            investmentRepository.save(investment);
            createContract(investment, exchange);
        }
    }

    public void updateInvestment(Integer investmentId, InvestmentDTO investmentDTO) {
        Investment investment = investmentRepository.findInvestmentById(investmentId);
        if (investment == null) {
            throw new ApiException("Investment not found");
        }
        if (investmentDTO.getIsRecurring()) {
            if (investmentDTO.getRecurringAmount() == null || investmentDTO.getRecurringYears() == null) {
                throw new ApiException("Recurring amount and years must be provided");
            }
            investment.setRoundType(investmentDTO.getRoundType());
            investment.setEffectiveDate(investmentDTO.getEffectiveDate());
            investment.setInvestment_amount(investmentDTO.getInvestment_amount());
            investment.setPaymentMethod(investmentDTO.getPaymentMethod());
            investment.setIsRecurring(true);
            investment.setMinimumInvestmentPeriod(investmentDTO.getMinimumInvestmentPeriod());
            investment.setRecurringAmount(investmentDTO.getRecurringAmount());
            investment.setRecurringYears(investmentDTO.getRecurringYears());
        } else {
            if (investmentDTO.getRecurringAmount() != null || investmentDTO.getRecurringYears() != null) {
                throw new ApiException("Recurring amount and years can not be set for non-recurring investment.");
            }
            investment.setRoundType(investmentDTO.getRoundType());
            investment.setEffectiveDate(investmentDTO.getEffectiveDate());
            investment.setInvestment_amount(investmentDTO.getInvestment_amount());
            investment.setPaymentMethod(investmentDTO.getPaymentMethod());
            investment.setMinimumInvestmentPeriod(investmentDTO.getMinimumInvestmentPeriod());
            investment.setIsRecurring(false);
            investment.setRecurringAmount(null);
            investment.setRecurringYears(null);
        }
        investmentRepository.save(investment);
    }

    public void deleteInvestment(Integer investmentId) {
        Investment investment = investmentRepository.findInvestmentById(investmentId);
        if (investment == null) {
            throw new ApiException("Investment not found");
        }
        investmentRepository.delete(investment);
    }

    public void createContract(Investment investment, String exchange) throws ApiException {
        if (!investment.getIsRecurring()) {
            ContractDTO contractDTO = new ContractDTO(investment.getEffectiveDate(), investment.getInvestor().getName(), investment.getStartup().getName(), investment.getInvestment_amount(), investment.getPaymentMethod(), investment.getMinimumInvestmentPeriod(), exchange, null, null);
            fileService.createContract(contractDTO, "one_time_basis");
        } else {
            ContractDTO contractDTO = new ContractDTO(investment.getEffectiveDate(), investment.getInvestor().getName(), investment.getStartup().getName(), investment.getInvestment_amount(), investment.getPaymentMethod(), investment.getMinimumInvestmentPeriod(), exchange, investment.getRecurringYears(), investment.getRecurringAmount());
            fileService.createContract(contractDTO, "recurring_basis");
        }
    }

    public byte[] viewContract(Integer investment_id) {
        Investment investment = investmentRepository.findInvestmentById(investment_id);
        if (investment == null) {
            throw new ApiException("Investment not found");
        }
        if (investment.getIsRecurring()) {
            return s3Service.downloadFile("recurring_basis" + "_" + investment.getStartup().getName().trim().replaceAll("\\s+", "_")
                    + "_" + investment.getInvestor().getName().trim().replaceAll("\\s+", "_") + ".pdf");
        } else {
            return s3Service.downloadFile("one_time_basis" + "_" + investment.getStartup().getName().trim().replaceAll("\\s+", "_")
                    + "_" + investment.getInvestor().getName().trim().replaceAll("\\s+", "_") + ".pdf");
        }
    }


}
