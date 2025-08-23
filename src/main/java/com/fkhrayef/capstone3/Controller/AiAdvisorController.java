package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.DTOin.AiAdvisorDTO;
import com.fkhrayef.capstone3.Service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ai-advisor")
@RequiredArgsConstructor
public class AiAdvisorController {

    private final AiService aiService;

    @PostMapping("/validate-idea")
    public ResponseEntity<?> validateBusinessIdea(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "business_idea_validation");
    }

    @PostMapping("/analyze-market")
    public ResponseEntity<?> analyzeMarket(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "target_market_analysis");
    }

    @PostMapping("/competitive-analysis")
    public ResponseEntity<?> getCompetitiveAnalysis(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "competitive_analysis");
    }

    @PostMapping("/revenue-model")
    public ResponseEntity<?> getRevenueModelAdvice(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "revenue_model_advice");
    }

    @PostMapping("/funding-strategy")
    public ResponseEntity<?> getFundingStrategy(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "funding_strategy");
    }

    @PostMapping("/financial-planning")
    public ResponseEntity<?> getFinancialPlanning(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "financial_planning");
    }

    @PostMapping("/mvp-strategy")
    public ResponseEntity<?> getMvpStrategy(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "mvp_strategy");
    }

    @PostMapping("/growth-strategy")
    public ResponseEntity<?> getGrowthStrategy(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "growth_strategy");
    }

    @PostMapping("/team-building")
    public ResponseEntity<?> getTeamBuildingAdvice(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "team_building");
    }

    @PostMapping("/legal-structure")
    public ResponseEntity<?> getLegalStructure(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "legal_structure");
    }

    @PostMapping("/risk-assessment")
    public ResponseEntity<?> getRiskAssessment(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "risk_assessment");
    }

    @PostMapping("/general-advice")
    public ResponseEntity<?> getGeneralAdvice(@Valid @RequestBody AiAdvisorDTO request) {
        return getAdvice(request, "general_startup_advice");
    }

    // Single helper method for all endpoints
    private ResponseEntity<?> getAdvice(AiAdvisorDTO request, String template) {
        String response = aiService.chat(template, request.getPrompt(), request.getStartupId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
