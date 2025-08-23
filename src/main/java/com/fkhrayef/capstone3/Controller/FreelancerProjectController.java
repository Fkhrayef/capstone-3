package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.FreelancerProjectDTO;
import com.fkhrayef.capstone3.Service.FreelancerProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/freelancer-project")
@RequiredArgsConstructor
public class FreelancerProjectController {
    private final FreelancerProjectService freelancerProjectService;

    @GetMapping("/get/startup/project/{startup_id}")
    public ResponseEntity<?> getAllFreelancerProject(@PathVariable Integer startup_id) {
        return ResponseEntity.status(200).body(freelancerProjectService.getAllFreelancerProject(startup_id));
    }

    @PostMapping("/add/project/to/{startup_id}/with/{freelance_id}")
    public ResponseEntity<?> addFreelancerProjectByStartup(@PathVariable Integer startup_id,
                                                           @PathVariable Integer freelance_id,
                                                           @Valid @RequestBody FreelancerProjectDTO projectDTO) {
        freelancerProjectService.addFreelancerProjectByStartup(startup_id, freelance_id, projectDTO);
        return ResponseEntity.status(200).body(new ApiResponse("project requested from freelancer, pending freelancer response"));
    }

    @PutMapping("/update/freelance/{freelance_id}/accept/{project_id}")
    public ResponseEntity<?> freelancerAcceptFreelancerProject(@PathVariable Integer freelance_id,
                                                               @PathVariable Integer project_id) {
        freelancerProjectService.freelancerAcceptFreelancerProject(freelance_id, project_id);
        return ResponseEntity.status(200).body(new ApiResponse("freelance with id:" + freelance_id + " accepted project with id:" + project_id));
    }


    @PutMapping("/update/freelance/{freelance_id}/reject/{project_id}")
    public ResponseEntity<?> freelancerRejectFreelancerProject(@PathVariable Integer freelance_id,
                                                               @PathVariable Integer project_id) {
        freelancerProjectService.freelancerRejectFreelancerProject(freelance_id, project_id);
        return ResponseEntity.status(200).body(new ApiResponse("freelance with id:" + freelance_id + " rejected project with id:" + project_id));
    }


    @PutMapping("/update/startup/{startup_id}/cancel/{project_id}")
    public ResponseEntity<?> startupCancelFreelanceRequest(@PathVariable Integer startup_id,
                                                           @PathVariable Integer project_id) {
        freelancerProjectService.startupCancelFreelanceRequest(startup_id, project_id);
        return ResponseEntity.status(200).body(new ApiResponse("project request with id:" + project_id + " cancelled by start up with id:" + startup_id));
    }


    @PutMapping("/update/{project_id}/by/{startup_id}")
    public ResponseEntity<?> updateFreelancerProject(@PathVariable("project_id") Integer projectId,
                                                     @PathVariable("startup_id") Integer startupId,
                                                     @RequestBody @Valid FreelancerProjectDTO freelancerProjectDTO) {
        freelancerProjectService.updateFreelancerProject(projectId, startupId, freelancerProjectDTO);
        return ResponseEntity.status(200).body(new ApiResponse("project updated successfully"));
    }


    @DeleteMapping("/delete/{project_id}/by/{startup_id}")
    public ResponseEntity<?> deleteFreelancerProject(@PathVariable("project_id") Integer projectId,
                                                     @PathVariable("startup_id") Integer startupId) {
        freelancerProjectService.deleteFreelancerProject(projectId, startupId);
        return ResponseEntity.status(200).body(new ApiResponse("project deleted successfully"));
    }


}
