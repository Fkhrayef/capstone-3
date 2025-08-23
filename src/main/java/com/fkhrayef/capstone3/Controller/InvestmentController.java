package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.InvestmentDTO;
import com.fkhrayef.capstone3.Service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/investment")
public class InvestmentController {

    final InvestmentService investmentService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllInvestments() {
        return ResponseEntity.status(HttpStatus.OK.value()).body(investmentService.getAllInvestments());
    }

    @PostMapping("/add/{exchange}")
    public ResponseEntity<?> addInvestment(@RequestBody @Valid InvestmentDTO investmentDTO, @PathVariable String exchange) {
        investmentService.createInvestment(investmentDTO, exchange);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ApiResponse("Investment added successfully"));
    }

    @PutMapping("/update/{investment_id}")
    public ResponseEntity<?> updateInvestment(@RequestBody @Valid InvestmentDTO investmentDTO, @PathVariable Integer investment_id) {
        investmentService.updateInvestment(investment_id, investmentDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ApiResponse("Investment updated successfully"));
    }

    @DeleteMapping("/delete/{investment_id}")
    public ResponseEntity<?> deleteInvestment(@PathVariable Integer investment_id) {
        investmentService.deleteInvestment(investment_id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ApiResponse("Investment deleted successfully"));
    }

    @GetMapping("/view/{investment_id}")
    public ResponseEntity<?> viewContract(@PathVariable Integer investment_id) {
        byte[] fileContent = investmentService.viewContract(investment_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("contract.pdf").build());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

    }
}
