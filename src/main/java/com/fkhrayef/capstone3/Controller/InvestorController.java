package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.Model.Investor;
import com.fkhrayef.capstone3.Service.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/investor")
@RequiredArgsConstructor
public class InvestorController {
    private final InvestorService investorService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllInvestors() {
        return ResponseEntity.status(200).body(investorService.getAllInvestors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addInvestor(@Valid @RequestBody Investor investor) {
        investorService.addInvestor(investor);
        return ResponseEntity.status(200).body(new ApiResponse("investor added successfully"));
    }

    @PutMapping("/update/{investor_id}")
    public ResponseEntity<?> updateInvestor(@PathVariable Integer investor_id, @Valid @RequestBody Investor investor) {
        investorService.updateInvestor(investor_id, investor);
        return ResponseEntity.status(200).body(new ApiResponse("investor updated successfully"));
    }

    @DeleteMapping("/delete/{investor_id}")
    public ResponseEntity<?> deleteInvestor(@PathVariable Integer investor_id) {
        investorService.deleteInvestor(investor_id);
        return ResponseEntity.status(200).body(new ApiResponse("investor delete successfully"));
    }
}
