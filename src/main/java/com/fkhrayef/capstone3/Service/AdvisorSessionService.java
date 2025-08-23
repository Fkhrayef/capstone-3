package com.fkhrayef.capstone3.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOin.AdvisorSessionDTO;
import com.fkhrayef.capstone3.Model.*;
import com.fkhrayef.capstone3.Repository.AdvisorRepository;
import com.fkhrayef.capstone3.Repository.AdvisorSessionRepository;
import com.fkhrayef.capstone3.Repository.PaymentRepository;
import com.fkhrayef.capstone3.Repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvisorSessionService {

    private static final Logger logger = LoggerFactory.getLogger(AdvisorSessionService.class);

    private final AdvisorSessionRepository advisorSessionRepository;
    private final StartupRepository startupRepository;
    private final AdvisorRepository advisorRepository;
    private final WebexService webexService;
    private final FirefliesAiApiService firefliesAiApiService;
    private final PaymentRepository paymentRepository;
    private final WhatsappService whatsappService;

    ///  1- get sessions of one startup
    public List<AdvisorSession> getAllAdvisorSessionsFromStartup(Integer startupId) {

        // 1- check if startup exist:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }
        return advisorSessionRepository.findAdvisorSessionByStartupId(startup.getId());
    }

    /// 2- startup create session targeting a specific advisor (single step)
    public void addAdvisorSessionByStartup(Integer startupId, Integer advisorId, AdvisorSessionDTO dto) {

        // 1- check if startup exist:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        //  2- prevent startup from add duplicate sessions
        AdvisorSession session =
                advisorSessionRepository.
                        findAdvisorSessionByStartupIdAndStartDateAndNotes
                                (startup.getId(), dto.getStartDate(), dto.getNotes());
        if (session != null) {
            throw new ApiException("this session already exist");
        }

        // 3- check if advisor exist:
        Advisor advisor = advisorRepository.findAdvisorById(advisorId);
        if (advisor == null) {
            throw new ApiException("advisor not found");
        }

        // 4- add the request values:
        AdvisorSession advisorSession = new AdvisorSession();
        advisorSession.setTitle(dto.getTitle());
        advisorSession.setStartDate(dto.getStartDate());
        advisorSession.setDuration_minutes(dto.getDuration_minutes());
        advisorSession.setNotes(dto.getNotes());
        advisorSession.setStatus("pending");

        // 5- link session with startup & advisor then save the session:
        advisorSession.setStartup(startup);
        advisorSession.setAdvisor(advisor);
        advisorSessionRepository.save(advisorSession);

        // 6- Send WhatsApp notification to advisor about new request
        try {
            if (advisor.getPhone() != null) {
                String message = "🔔 طلب جلسة استشارية جديد\n" +
                        "الشركة: " + startup.getName() + "\n" +
                        "الجلسة: " + dto.getTitle() + "\n" +
                        "يرجى مراجعة الطلب والرد عليه";
                whatsappService.sendTextMessage(message, advisor.getPhone());
            }
        } catch (Exception ex) {
            // Log error but don't fail the main operation
            logger.error("Failed to send WhatsApp notification: {}", ex.getMessage());
        }
    }

    /// 3- advisor accepts session:
    public void advisorAcceptAdvisorSession(Integer advisorId, Integer sessionId) {

        // 1- check if advisor exist:
        Advisor advisor = advisorRepository.findAdvisorById(advisorId);
        if (advisor == null) {
            throw new ApiException("advisor not found");
        }

        // 2- check if session exist:
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // 3- check if session and advisor related:
        if (session.getAdvisor().getId() == null || !session.getAdvisor().getId().equals(advisor.getId())) {
            throw new ApiException("session & advisor not belong to each other");
        }

        // 4- check from session status:
        if (!session.getStatus().equals("pending")) {
            throw new ApiException("session status must be pending");
        }

        // change session status:
        session.setStatus("scheduled"); // scheduled successfully
        advisorSessionRepository.save(session);
    }


    /// 4- advisor reject session:
    public void advisorRejectAdvisorSession(Integer advisorId, Integer sessionId) {

        // 1- check if advisor exist:
        Advisor advisor = advisorRepository.findAdvisorById(advisorId);
        if (advisor == null) {
            throw new ApiException("advisor not found");
        }

        // 2- check if session exist:
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // 3- check if session and advisor related:
        if (session.getAdvisor().getId() == null || !session.getAdvisor().getId().equals(advisor.getId())) {
            throw new ApiException("session & advisor not belong to each other");
        }

        // 4- check from session status:
        if (!session.getStatus().equals("pending")) {
            throw new ApiException("session status must be pending");
        }

        // change session status:
        session.setStatus("rejected"); // Advisor rejected this session
        advisorSessionRepository.save(session);
    }


    /// 5- allow startup to cancel their request:
    public void startupCancelAdvisorRequest(Integer startupId, Integer sessionId) {

        // 1- check if startup exist:
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 2- check if session exist:
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // 3- check session belongs to same startup
        if (session.getStartup().getId() == null
                || !session.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("session and startup not belong to each other");
        }

        // 4- check if status of session still pending:
        if (!session.getStatus().equals("pending")) {
            throw new ApiException("status should be pending to cancel advising session");
        }

        session.setStatus("cancelled"); // Mark as cancelled, keep advisor for audit history
        advisorSessionRepository.save(session);
    }

    ///  6- update advisor session;
    public void updateAdvisorSession(Integer sessionId, Integer startupId, AdvisorSessionDTO dto) {

        // 1- check session exists
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // 2- check startup exists
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 3- ownership
        if (session.getStartup() == null || !session.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("session and startup not belong to each other");
        }

        // 4- only allow editing when pending
        if (!"pending".equals(session.getStatus())) {
            throw new ApiException("only pending sessions can be updated");
        }

        // 5- prevent duplicate
        AdvisorSession dup = advisorSessionRepository
                .findAdvisorSessionByStartupIdAndStartDateAndNotes(
                        startupId, dto.getStartDate(), dto.getNotes());

        if (dup != null
                && dup.getId() != null
                && !dup.getId().equals(session.getId())
                && dup.getStartup() != null
                && dup.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("another session with same startDate and notes already exists");
        }

        // 7- apply updates
        session.setStartDate(dto.getStartDate());
        session.setDuration_minutes(dto.getDuration_minutes());
        session.setNotes(dto.getNotes());

        // 8- save
        advisorSessionRepository.save(session);
    }


    ///  7- delete advisor session by startup:
    public void deleteAdvisorSession(Integer sessionId, Integer startupId) {

        // 1- check session exists
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // 2- check startup exists
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("startup not found");
        }

        // 3- check ownership
        if (session.getStartup() == null || !session.getStartup().getId().equals(startup.getId())) {
            throw new ApiException("session and startup not belong to each other");
        }

        // 4- check status
        if (!"pending".equals(session.getStatus())) {
            throw new ApiException("only pending sessions can be deleted");
        }

        // 5- break relations
        session.setAdvisor(null);
        session.setStartup(null);

        // 6- delete session
        advisorSessionRepository.delete(session);
    }

    public void startMeeting(Integer sessionId) {
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        // Check from session's status ('confirmed' if payment was successful):
        boolean isConfirmed = advisorSessionRepository
                .existsByIdAndStatus(session.getId(), "confirmed");
        if (!isConfirmed) {
            throw new ApiException("Payment for advising session:" + sessionId + " is not confirmed");
        }

        // Start meeting and save url and meeting id
        try (Response response = webexService.startMeeting(session.getTitle(), session.getStartDate(), session.getDuration_minutes(), getAllEmails(sessionId))) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);


                session.setMeeting_id(root.get("id").asText());
                session.setMeeting_url(root.get("webLink").asText());
            }
        } catch (Exception e) {
            throw new ApiException("Failed to start meeting");
        }
        advisorSessionRepository.save(session);

    }

    public String getSummary(String meetingLink) {
        return firefliesAiApiService.getMeetingSummary(meetingLink);
    }

    public String getActionItems(String meetingLink) {
        return firefliesAiApiService.getActionItems(meetingLink);
    }

    public String getBulletGist(String meetingLink) {
        return firefliesAiApiService.getBulletGist(meetingLink);
    }

    public List<String> getAllEmails(Integer sessionId) {
        AdvisorSession advisorSession = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (advisorSession == null) {
            throw new ApiException("session not found");
        }
        List<String> emails = new ArrayList<>();
        if (advisorSession.getAdvisor().getEmail() == null) {
            throw new ApiException("advisor email was not found");
        }
        emails.add(advisorSession.getAdvisor().getEmail());
        for (Founder founder : advisorSession.getStartup().getFounders()) {
            if (founder.getEmail() != null) {
                emails.add(founder.getEmail());
            }
        }
        return emails;
    }

    public void deleteMeeting(Integer sessionId) {
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }
        webexService.deleteMeeting(session.getMeeting_id());
        session.setMeeting_id(null);
        session.setMeeting_url(null);
        advisorSessionRepository.save(session);
    }

    public List<AdvisorSession> getAllAdvisorSessionsFromEmail(String email) {
        List<AdvisorSession> sessions = new ArrayList<>();
        for (AdvisorSession session : advisorSessionRepository.findAll()) {
            if (getAllEmails(session.getId()).contains(email)) {
                sessions.add(session);
            }
        }
        return sessions;
    }

    public String getAudioUrl(String meetingLink) {
        return firefliesAiApiService.getAudioUrl(meetingLink);
    }

    public byte[] addToCalender(Integer sessionId) {
        AdvisorSession session = advisorSessionRepository.findAdvisorSessionById(sessionId);
        if (session == null) {
            throw new ApiException("session not found");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        String startTime = session.getStartDate().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
        String endTime = session.getStartDate().plusMinutes(session.getDuration_minutes()).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
        String createdTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(formatter);

        String icalContent = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\r\n" +
                "PRODID:-//StartHub//Advisor session//EN\n" +
                "BEGIN:VEVENT\n" +
                "UID:" + System.currentTimeMillis() + session.getStartup().getName() + "\n" +
                "DTSTAMP:" + createdTime + "\n" +
                "DTSTART:" + startTime + "\n" +
                "DTEND:" + endTime + "\n" +
                "SUMMARY:" + session.getTitle() + "\n" +
                "DESCRIPTION:" + session.getNotes() + "\n" +
                "URL: " + session.getMeeting_url() + "\n" +
                "STATUS:CONFIRMED\n" +
                "SEQUENCE:0\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR\n";

        return icalContent.getBytes(StandardCharsets.UTF_8);

    }

    public List<AdvisorSession> getByStatusAndAdvisor(String status, Integer advisorId) {
        Advisor advisor = advisorRepository.findAdvisorById(advisorId);
        if (advisor == null) {
            throw new ApiException("advisor not found");
        }
        List<AdvisorSession> sessions = advisorSessionRepository.findAdvisorSessionsByAdvisorAndStatus(advisor, status);
        if (sessions == null) {
            throw new ApiException("no session was found with that status");
        }
        return sessions;
    }

}
