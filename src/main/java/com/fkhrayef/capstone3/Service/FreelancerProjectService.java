package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.FreelancerProjectDTO;
import com.fkhrayef.capstone3.Model.Freelancer;
import com.fkhrayef.capstone3.Model.FreelancerProject;
import com.fkhrayef.capstone3.Model.Startup;
import com.fkhrayef.capstone3.Repository.FreelancerProjectRepository;
import com.fkhrayef.capstone3.Repository.FreelancerRepository;
import com.fkhrayef.capstone3.Repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreelancerProjectService {

    private static final Logger logger = LoggerFactory.getLogger(FreelancerProjectService.class);

    private final StartupRepository startupRepository;
    private final FreelancerProjectRepository freelancerProjectRepository;
    private final FreelancerRepository freelancerRepository;
    private final WhatsappService whatsappService;

    // list of freelancerProject from certain startup:
    public List<FreelancerProject> getAllFreelancerProject(Integer startupId) {

        // 1- find the desired startup:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 2- fetch the freelancerProjects from desired startup:
        List<FreelancerProject> freelancerProjects = freelancerProjectRepository.giveMeFreelancerProjectByStartupId(startupId);

        return freelancerProjects;
    }


    /// add project by startup with a specific freelancer
    public void addFreelancerProjectByStartup(Integer startupId, Integer freelancerId, FreelancerProjectDTO freelancerProjectDTO) {

        //1- check if startup exist:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 2- prevent startup from have multiple project with same name & description:
        FreelancerProject project = freelancerProjectRepository.
                findFreelancerProjectByProjectNameAndDescriptionAndStartupId
                        (freelancerProjectDTO.getProjectName(), freelancerProjectDTO.getDescription(), startup.getId());
        if (project != null) {
            throw new ApiException("there is similar project");
        }

        // 3- check freelancer exist
        Freelancer freelancer = freelancerRepository.findFreelancerById(freelancerId);
        if (freelancer == null) {
            throw new ApiException("freelancer not found");
        }

        // 4- add values for the project:
        FreelancerProject freelancerProject = new FreelancerProject();
        freelancerProject.setProjectName(freelancerProjectDTO.getProjectName());
        freelancerProject.setDescription(freelancerProjectDTO.getDescription());
        freelancerProject.setStartDate(freelancerProjectDTO.getStartDate());
        freelancerProject.setEndDate(freelancerProjectDTO.getEndDate());
        freelancerProject.setStatus("pending"); // Start as pending, becomes active after payment

        // Add pricing fields
        freelancerProject.setEstimatedHours(freelancerProjectDTO.getEstimatedHours());

        // 5- link the project with the startup and freelancer & save:
        freelancerProject.setStartup(startup);
        freelancerProject.setFreelancer(freelancer);
        freelancerProjectRepository.save(freelancerProject);

        // 6- Send WhatsApp notification to freelancer about new project request
        try {
            if (freelancer.getPhone() != null) {
                String message = "🔔 طلب مشروع جديد\n" +
                        "الشركة: " + startup.getName() + "\n" +
                        "المشروع: " + freelancerProjectDTO.getProjectName() + "\n" +
                        "يرجى مراجعة الطلب والرد عليه";
                whatsappService.sendTextMessage(message, freelancer.getPhone());
            }
        } catch (Exception ex) {
            // Log error but don't fail the main operation
            logger.error("Failed to send WhatsApp notification: {}", ex.getMessage());
        }

    }

    /// make the freelancer accepts the project:
    public void freelancerAcceptFreelancerProject(Integer freelanceId, Integer projectId) {

        // 1- check if freelancer exist:
        Freelancer freelancer = freelancerRepository.findFreelancerById(freelanceId);
        if (freelancer == null) {
            throw new ApiException("freelancer not exist");
        }

        // 2- check if project exist;
        FreelancerProject project = freelancerProjectRepository.findFreelancerProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        // 3- check if project & freelancer are belong to each other:
        if (project.getFreelancer().getId() == null || !project.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ApiException("freelancer does not belong to this project");
        }

        // 4- check from project status;
        if (!project.getStatus().equals("pending")) {
            throw new ApiException("project status should be pending");
        }

        // 5- change project status:
        project.setStatus("accepted");
        freelancerProjectRepository.save(project);
    }


    /// make freelancer reject project request:
    public void freelancerRejectFreelancerProject(Integer freelanceId, Integer projectId) {

        // 1- check if freelancer exist:
        Freelancer freelancer = freelancerRepository.findFreelancerById(freelanceId);
        if (freelancer == null) {
            throw new ApiException("freelancer not exist");
        }

        // 2- check if project exist;
        FreelancerProject project = freelancerProjectRepository.findFreelancerProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        // 3- check if project & freelancer are belong to each other:
        if (project.getFreelancer().getId() == null || !project.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ApiException("freelancer does not belong to this project");
        }

        // 4- check from project status;
        if (!project.getStatus().equals("pending")) {
            throw new ApiException("project status should be pending");
        }

        // 5- change project status:
        project.setStatus("rejected");
        freelancerProjectRepository.save(project);
    }


    /// allow the startup to cancel request & break relation with freelance:
    public void startupCancelFreelanceRequest(Integer startupId, Integer projectId) {

        // 1- check if startup exist:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 2- check if project exist:
        FreelancerProject project = freelancerProjectRepository.findFreelancerProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        // 3- check the project belong to the same startup
        if (project.getStartup().getId() == null
                || !project.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("project and startup not belong to each other");
        }

        // 5- check if status still pending from freelancer:
        if (!project.getStatus().equals("pending")) {
            throw new ApiException("status should be pending to cancel request");
        }

        project.setStatus("cancelled");
        freelancerProjectRepository.save(project);
    }


    public void updateFreelancerProject(Integer projectId, Integer startupId, FreelancerProjectDTO dto) {

        // 1- check project exists
        FreelancerProject project = freelancerProjectRepository.findFreelancerProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        // 2- check startup exists
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 3- check ownership (avoid NPE)
        if (project.getStartup() == null || !project.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("project and startup not belong to each other");
        }

        // 4- validate dates
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new ApiException("start date must be before or equal to end date");
        }

        // 5- prevent duplicate name+description within the same startup (excluding this project)
        FreelancerProject dup = freelancerProjectRepository
                .findFreelancerProjectByProjectNameAndDescriptionAndStartupId
                        (dto.getProjectName(), dto.getDescription(), startupId);
        if (dup != null
                && dup.getId() != null
                && !dup.getId().equals(project.getId())
                && dup.getStartup() != null
                && dup.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("there is similar project for this startup");
        }

        // 6- apply updates (status unchanged here)
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        // Update pricing fields
        project.setEstimatedHours(dto.getEstimatedHours());

        // 7- save
        freelancerProjectRepository.save(project);
    }


    ///  delete project with in both sides (startup & freelancer)
    public void deleteFreelancerProject(Integer projectId, Integer startupId) {

        // 1-  check project exists
        FreelancerProject project = freelancerProjectRepository.findFreelancerProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        // 2- check from startup:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 3- check ownership
        if (project.getStartup() == null || !project.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("project and startup not belong to each other");
        }

        // 4- only allow deletion when pending
        if (!"pending".equals(project.getStatus())) {
            throw new ApiException("only pending projects can be deleted");
        }

        // 5- break relations then delete
        project.setFreelancer(null);
        project.setStartup(null);
        freelancerProjectRepository.delete(project);

    }


}
