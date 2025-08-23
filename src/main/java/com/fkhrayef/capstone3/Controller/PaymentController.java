package com.fkhrayef.capstone3.Controller;

import com.fkhrayef.capstone3.Api.ApiResponse;
import com.fkhrayef.capstone3.DTOin.PaymentRequest;
import com.fkhrayef.capstone3.DTOout.MoyasarPaymentResponseDTO;
import com.fkhrayef.capstone3.DTOout.PaymentCreationResponseDTO;
import com.fkhrayef.capstone3.Model.Payment;
import com.fkhrayef.capstone3.Model.Subscription;
import com.fkhrayef.capstone3.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Simple payment processing
    @PostMapping("/card")
    public ResponseEntity<MoyasarPaymentResponseDTO> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        MoyasarPaymentResponseDTO response = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }

    // Pay for freelancer project
    @PostMapping("/freelancer-project/startup/{startupId}/project/{projectId}")
    public ResponseEntity<?> payForProject(@PathVariable Integer startupId,
                                           @PathVariable Integer projectId,
                                           @Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentCreationResponseDTO response = paymentService.createFreelancerProjectPayment(startupId, projectId, paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Pay for advisor session
    @PostMapping("/advisor-session/startup/{startupId}/session/{sessionId}")
    public ResponseEntity<?> payForSession(@PathVariable Integer startupId,
                                           @PathVariable Integer sessionId,
                                           @Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentCreationResponseDTO response = paymentService.createAdvisorSessionPayment(startupId, sessionId, paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Pay for subscription
    @PostMapping("/subscription/startup/{startupId}/plan/{planType}/billing/{billingCycle}")
    public ResponseEntity<?> payForSubscription(@PathVariable Integer startupId,
                                                @PathVariable String planType,
                                                @PathVariable String billingCycle,
                                                @Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentCreationResponseDTO response = paymentService.createSubscriptionPayment(startupId, planType, billingCycle, paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Moyasar callback endpoint (just shows status)
    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestParam String id,
                                            @RequestParam String status,
                                            @RequestParam(required = false) String message) {
        // NO business logic - just return a message
        return ResponseEntity.ok(new ApiResponse(
                "Payment " + status + " processed. Moyasar ID: " + id +
                        (message != null ? ". Message: " + message : "")
        ));
    }

    // Moyasar webhook endpoint (handles business logic)
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload) {
        // Process the webhook payload (includes secret token validation)
        paymentService.handleWebhook(payload);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Webhook processed successfully"));
    }

    // Simple payment status check endpoint (for frontend use)
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentId) {
        String status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Payment status: " + status));
    }

    // Get payment by ID
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable Integer paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

    // Cancel subscription
    @PostMapping("/subscription/{startupId}/cancel")
    public ResponseEntity<?> cancelSubscription(@PathVariable Integer startupId) {
        paymentService.cancelSubscription(startupId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Subscription cancelled successfully"));
    }

    // Get subscription status
    @GetMapping("/subscription/{startupId}/status")
    public ResponseEntity<?> getSubscriptionStatus(@PathVariable Integer startupId) {
        Subscription subscription = paymentService.getSubscriptionStatus(startupId);
        return ResponseEntity.status(HttpStatus.OK).body(subscription);
    }

    // Get expiring subscriptions (admin endpoint)
    @GetMapping("/subscription/expiring")
    public ResponseEntity<?> getExpiringSubscriptions() {
        List<Subscription> expiringSubscriptions = paymentService.getExpiringSubscriptions();
        return ResponseEntity.status(HttpStatus.OK).body(expiringSubscriptions);
    }
}
