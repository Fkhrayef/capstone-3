package com.fkhrayef.capstone3.Service;


import com.fkhrayef.capstone3.Api.ApiException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Service
public class WhatsappService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappService.class);

    @Value("${whatsapp.ultramsg.key}")
    private String whatsappKey;
    private final OkHttpClient client;

    public WhatsappService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }


    public void sendTextMessage(String message, String phoneNumber) throws ApiException {

        RequestBody body = new FormBody.Builder()
                .add("token", whatsappKey)
                .add("to", phoneNumber)
                .add("body", message)
                .build();

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance139636/messages/chat")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiException("Unexpected error while sending message to Whatsapp: " + response.code());
            }
            String responseBody = response.body().string();
            logger.debug("WhatsApp API response: {}", responseBody);
        } catch (Exception e) {
            throw new ApiException("Unexpected error while sending message to Whatsapp");
        }

    }
}
