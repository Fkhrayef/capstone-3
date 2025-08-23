package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.StartupDTO;
import com.fkhrayef.capstone3.Service.StartupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/startups")
@RequiredArgsConstructor
public class StartupController {

    private final StartupService startupService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllStartups() {
        return ResponseEntity.status(HttpStatus.OK).body(startupService.getAllStartups());
    }

    @PostMapping("/add/{founderId}")
    public ResponseEntity<?> addStartup(@PathVariable Integer founderId, @Valid @RequestBody StartupDTO startupDTO) {
        startupService.addStartup(founderId, startupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Startup added successfully"));
    }

    @PostMapping("/{startupId}/add-founder/{founderId}")
    public ResponseEntity<?> addFounderToStartup(@PathVariable Integer startupId, @PathVariable Integer founderId, @RequestParam Double equityPercentage) {
        startupService.addFounderToStartup(startupId, founderId, equityPercentage);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Founder added to startup successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStartup(@PathVariable Integer id, @Valid @RequestBody StartupDTO startupDTO) {
        startupService.updateStartup(id, startupDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Startup updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStartup(@PathVariable Integer id) {
        startupService.deleteStartup(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Startup deleted successfully"));
    }
}
