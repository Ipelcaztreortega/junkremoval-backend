package com.junktwinsllc.junkremoval.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
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

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Stripe.apiKey = secretKey;

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (session != null) {
                    String jobIdStr = session.getMetadata().get("jobId");
                    if (jobIdStr != null) {
                        long amount = session.getAmountTotal() != null ? session.getAmountTotal() : 0;
                        markJobAsPaid(Long.parseLong(jobIdStr), amount);
                    }
                }
            }
            case "payment_intent.succeeded" -> {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (intent != null) {
                    String jobIdStr = intent.getMetadata().get("jobId");
                    if (jobIdStr != null) {
                        markJobAsPaid(Long.parseLong(jobIdStr), intent.getAmountReceived());
                    }
                }
            }
        }

        return ResponseEntity.ok("received");
    }

    private void markJobAsPaid(Long jobId, long amountInCents) {
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus("PAID");
            jobRepository.save(job);

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