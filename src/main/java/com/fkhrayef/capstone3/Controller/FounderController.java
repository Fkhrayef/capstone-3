package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.FounderDTO;
import com.fkhrayef.capstone3.Service.FounderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/founders")
@RequiredArgsConstructor
public class FounderController {

    private final FounderService founderService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllFounders() {
        return ResponseEntity.status(HttpStatus.OK).body(founderService.getAllFounders());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFounder(@Valid @RequestBody FounderDTO founderDTO) {
        founderService.addFounder(founderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Founder added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFounder(@PathVariable Integer id, @Valid @RequestBody FounderDTO founderDTO) {
        founderService.updateFounder(id, founderDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Founder updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFounder(@PathVariable Integer id) {
        founderService.deleteFounder(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Founder deleted successfully"));
    }
}
