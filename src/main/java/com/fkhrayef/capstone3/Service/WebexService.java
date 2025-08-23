package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WebexService {
    private static final Logger logger = LoggerFactory.getLogger(WebexService.class);

    @Value("${WEBEX_KEY}")
    private String webexKey;

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public WebexService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Response startMeeting(String title, LocalDateTime meetingDateTime, Integer durationMinutes, List<String> inviteEmails) throws ApiException {
        try {
            ZonedDateTime start = meetingDateTime.atZone(ZoneId.of("Asia/Riyadh"));
            ZonedDateTime end = start.plusMinutes(durationMinutes);

            String startDate = start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String endDate = end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            List<Map<String, String>> invitees = new ArrayList<>();

            Map<String, String> aiInvitee = new HashMap<>();
            aiInvitee.put("email", "fred@fireflies.ai");
            invitees.add(aiInvitee);

            for (String inviteEmail : inviteEmails) {
                Map<String, String> invitee = new HashMap<>();
                invitee.put("email", inviteEmail);
                invitees.add(invitee);
            }


            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("title", title);
            requestBody.put("start", startDate);
            requestBody.put("end", endDate);
            requestBody.put("unlockedMeetingJoinSecurity", "allowJoin");
            requestBody.put("invitees", invitees);

            String json = objectMapper.writeValueAsString(requestBody);
            logger.debug("Webex meeting request JSON: {}", json);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url("https://webexapis.com/v1/meetings")
                    .addHeader("Authorization", "Bearer " + webexKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                throw new ApiException("Meeting creation failed: " + response.code() + " - " + responseBody);
            }

            return response;
        } catch (Exception e) {
            throw new ApiException("Unexpected error while starting meeting: " + e.getMessage());
        }
    }

    public void deleteMeeting(String meetingId) throws ApiException {
        try {
            Request request = new Request.Builder()
                    .url("https://webexapis.com/v1/meetings/" + meetingId)
                    .addHeader("Authorization", "Bearer " + webexKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .delete()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new ApiException("Meeting deletion failed");
                }
            }
        } catch (Exception e) {
            throw new ApiException("Unexpected error while deleting meeting");
        }
    }
}