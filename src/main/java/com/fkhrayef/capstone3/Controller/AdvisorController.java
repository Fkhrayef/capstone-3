package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.AdvisorDTO;
import com.fkhrayef.capstone3.Service.AdvisorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advisor")
public class AdvisorController {

    private final AdvisorService advisorService;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllAdvisors() {
        return ResponseEntity.status(HttpStatus.OK.value()).body(advisorService.getAllAdvisors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAdvisor(@RequestBody @Valid AdvisorDTO advisorDTO) {
        advisorService.addAdvisor(advisorDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ApiResponse("Advisor added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdvisor(@RequestBody @Valid AdvisorDTO advisorDTO, @PathVariable Integer id) {
        advisorService.updateAdvisor(id, advisorDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ApiResponse("Advisor updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdvisor(@PathVariable Integer id) {
        advisorService.deleteAdvisor(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ApiResponse("Advisor deleted successfully"));
    }

}
