package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.AdvisorSessionDTO;
import com.fkhrayef.capstone3.Service.AdvisorSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/advisor-session")
@RequiredArgsConstructor
public class AdvisorSessionController {

    private final AdvisorSessionService advisorSessionService;

    // 1- get all sessions for a startup
    @GetMapping("/get/startup/{startup_id}")
    public ResponseEntity<?> getAllAdvisorSessionsByStartup(@PathVariable("startup_id") Integer startupId) {
        return ResponseEntity.status(200).body(advisorSessionService.getAllAdvisorSessionsFromStartup(startupId));
    }

    // 2- startup create new session with a specific advisor (single step)
    @PostMapping("/add/to/{startup_id}/with/{advisor_id}")
    public ResponseEntity<?> addAdvisorSessionByStartup(@PathVariable("startup_id") Integer startupId,
                                                        @PathVariable("advisor_id") Integer advisorId,
                                                        @Valid @RequestBody AdvisorSessionDTO dto) {
        advisorSessionService.addAdvisorSessionByStartup(startupId, advisorId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("advisor session created and assigned to advisor, pending advisor response"));
    }

    // 3- advisor accepts session
    @PutMapping("/update/advisor/{advisor_id}/accept/{session_id}")
    public ResponseEntity<?> advisorAcceptAdvisorSession(@PathVariable("advisor_id") Integer advisorId,
                                                         @PathVariable("session_id") Integer sessionId) {
        advisorSessionService.advisorAcceptAdvisorSession(advisorId, sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("advisor with id:" + advisorId + " accepted session with id:" + sessionId));
    }

    // 4- advisor rejects session
    @PutMapping("/update/advisor/{advisor_id}/reject/{session_id}")
    public ResponseEntity<?> advisorRejectAdvisorSession(@PathVariable("advisor_id") Integer advisorId,
                                                         @PathVariable("session_id") Integer sessionId) {
        advisorSessionService.advisorRejectAdvisorSession(advisorId, sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("advisor with id:" + advisorId + " rejected session with id:" + sessionId));
    }

    // 5- startup cancel their request (no advisor id needed)
    @PutMapping("/update/startup/{startup_id}/cancel/{session_id}")
    public ResponseEntity<?> startupCancelAdvisorRequest(@PathVariable("startup_id") Integer startupId,
                                                         @PathVariable("session_id") Integer sessionId) {
        advisorSessionService.startupCancelAdvisorRequest(startupId, sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("session with id:" + sessionId + " cancelled by startup with id:" + startupId));
    }

    // 6- update session (startup)
    @PutMapping("/update/{session_id}/by/{startup_id}")
    public ResponseEntity<?> updateAdvisorSession(@PathVariable("session_id") Integer sessionId,
                                                  @PathVariable("startup_id") Integer startupId,
                                                  @Valid @RequestBody AdvisorSessionDTO dto) {
        advisorSessionService.updateAdvisorSession(sessionId, startupId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("session updated successfully"));
    }

    // 7- delete session
    @DeleteMapping("/delete/{session_id}/by/{startup_id}")
    public ResponseEntity<?> deleteAdvisorSession(@PathVariable("session_id") Integer sessionId,
                                                  @PathVariable("startup_id") Integer startupId) {
        advisorSessionService.deleteAdvisorSession(sessionId, startupId);
        return ResponseEntity.status(200).body(new ApiResponse("session deleted successfully"));
    }

    @PostMapping("/create-meeting/{sessionId}")
    public ResponseEntity<?> startMeeting(@PathVariable Integer sessionId){
        advisorSessionService.startMeeting(sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Meeting was created successfully, Check your email for the meeting link"));
    }

    @DeleteMapping("/delete/meeting/{sessionId}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Integer sessionId){
        advisorSessionService.deleteMeeting(sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Meeting was deleted successfully"));
    }

    @GetMapping("/get/all-from-email/{email}")
    public ResponseEntity<?> getAllAdvisorSessionsFromEmail(@PathVariable String email){
        return ResponseEntity.status(200).body(advisorSessionService.getAllAdvisorSessionsFromEmail(email));
    }

    @GetMapping("/get/summary/")
    public ResponseEntity<?> getSummary(@RequestParam String link){
        return ResponseEntity.status(200).body(new ApiResponse(advisorSessionService.getSummary(link)));
    }

    @GetMapping("/get/bullet-gist/")
    public ResponseEntity<?> getBulletGist(@RequestParam String link){
        return ResponseEntity.status(200).body(new ApiResponse(advisorSessionService.getBulletGist(link)));
    }

    @GetMapping("/get/action-items/")
    public ResponseEntity<?> getActionItems(@RequestParam String link){
        return ResponseEntity.status(200).body(new ApiResponse(advisorSessionService.getActionItems(link)));
    }

    @GetMapping("/get/audio-url/")
    public ResponseEntity<?> getAudioUrl(@RequestParam String meetingLink){
        return ResponseEntity.status(200).body(new ApiResponse(advisorSessionService.getAudioUrl(meetingLink)));
    }

    @PostMapping("/add/calender-event/{sessionId}")
    public ResponseEntity<?> addToCalender(@PathVariable Integer sessionId){
        byte[] fileContent = advisorSessionService.addToCalender(sessionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/calendar"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("advisor-session-" + sessionId + ".ics").build());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/get/status/{status}/advisor/{advisorId}")
    public ResponseEntity<?> getByStatusAndAdvisor(@PathVariable String status, @PathVariable Integer advisorId){
        return ResponseEntity.status(200).body(advisorSessionService.getByStatusAndAdvisor(status, advisorId));
    }

}
