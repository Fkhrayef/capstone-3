package com.fkhrayef.capstone3.DTOout;

import com.fkhrayef.capstone3.Model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreationResponseDTO {

    private Payment payment; // Our internal payment record
    private String transactionUrl; // Moyasar payment link for user to complete payment
    private String moyasarPaymentId; // Moyasar payment ID for reference
    private String status; // Current payment status
    private String message; // Success/instruction message
}
