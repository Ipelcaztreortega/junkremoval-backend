package com.junktwinsllc.junkremoval.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.model.Payment;
import com.junktwinsllc.junkremoval.repository.JobRepository;
import com.junktwinsllc.junkremoval.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final JobRepository jobRepository;
    private final PaymentRepository paymentRepository;

    public WebhookController(JobRepository jobRepository, PaymentRepository paymentRepository) {
        this.jobRepository = jobRepository;
        this.paymentRepository = paymentRepository;
    }

    // Stripe calls this endpoint when a payment succeeds
    // Must be public — no JWT required (Stripe can't log in)
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Stripe.apiKey = secretKey;

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            // Reject anything that didn't come from Stripe
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (intent != null) {
                String jobIdStr = intent.getMetadata().get("jobId");
                if (jobIdStr != null) {
                    Long jobId = Long.parseLong(jobIdStr);
                    markJobAsPaid(jobId, intent.getAmountReceived());
                }
            }
        }

        return ResponseEntity.ok("received");
    }

    private void markJobAsPaid(Long jobId, long amountInCents) {
        jobRepository.findById(jobId).ifPresent(job -> {
            // Update job status
            job.setStatus("PAID");
            jobRepository.save(job);

            // Record payment if one doesn't exist yet
            if (!paymentRepository.existsByJobId(jobId)) {
                Payment payment = new Payment();
                payment.setJob(job);
                payment.setAmount(BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100)));
                payment.setStatus("PAID");
                payment.setPaymentMethod("CARD");
                payment.setPaidAt(LocalDateTime.now());
                paymentRepository.save(payment);
            }
        });
    }
}