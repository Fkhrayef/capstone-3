package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.FreelancerDTO;
import com.fkhrayef.capstone3.Service.FreelancerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/freelancer")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllFreelancers() {
        return ResponseEntity.status(200).body(freelancerService.getAllFreelancers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFreelancer(@Valid @RequestBody FreelancerDTO freelancer) {
        freelancerService.addFreelancer(freelancer);
        return ResponseEntity.status(200).body(new ApiResponse("freelancer added successfully"));
    }

    @PutMapping("/update/{freelancer_id}")
    public ResponseEntity<?> updateFreelancer(@PathVariable Integer freelancer_id,
                                              @Valid @RequestBody FreelancerDTO freelancer) {
        freelancerService.updateFreelancer(freelancer_id, freelancer);
        return ResponseEntity.status(200).body(new ApiResponse("freelancer updated successfully"));
    }

    @DeleteMapping("/delete/{freelancer_id}")
    public ResponseEntity<?> deleteFreelancer(@PathVariable Integer freelancer_id) {
        freelancerService.deleteFreelancer(freelancer_id);
        return ResponseEntity.status(200).body(new ApiResponse("freelancer delete successfully"));
    }

    @GetMapping("/get/freelance/{freelance_id}/request/status/{status}")
    public ResponseEntity<?> getAllFreelancerProjectByStatus(@PathVariable Integer freelance_id,
                                                             @PathVariable String status) {
        return ResponseEntity.status(200).body(freelancerService.getAllFreelancerProjectByStatus(freelance_id, status));
    }
}
