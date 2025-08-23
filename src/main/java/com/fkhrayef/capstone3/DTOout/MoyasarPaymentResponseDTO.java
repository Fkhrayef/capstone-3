package com.fkhrayef.capstone3.DTOout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoyasarPaymentResponseDTO {

    private String id; // Payment ID from Moyasar
    private String status; // paid, failed, pending, etc.
    private Integer amount; // Amount in smallest currency unit (halalas)
    private String currency; // SAR
    private String description; // Payment description
    private String created; // ISO date string
    private String updated; // ISO date string

    // Source information (card details)
    private MoyasarSourceDTO source;

    // Fee information
    private Integer fee; // Moyasar fee

    // Additional fields that might be in response
    private String callbackUrl;
    private String invoiceId;
    private String metadata; // JSON string for any additional data
    private String transaction_url; // Payment link for 3D Secure/authentication

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MoyasarSourceDTO {
        private String type; // card
        private String company; // visa, mastercard, etc.
        private String name; // Cardholder name
        private String number; // Masked card number like ****-****-****-1234
        private String gatewayId; // Gateway identifier
        private String referenceNumber; // Transaction reference
        private String transaction_url; // Payment URL for 3D Secure
    }
}
