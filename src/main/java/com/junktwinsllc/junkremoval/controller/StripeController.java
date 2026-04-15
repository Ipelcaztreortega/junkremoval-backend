package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.service.JobService;
import com.junktwinsllc.junkremoval.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/stripe-link")
public class StripeController {

    private final StripeService stripeService;
    private final JobService jobService;

    public StripeController(StripeService stripeService, JobService jobService) {
        this.stripeService = stripeService;
        this.jobService = jobService;
    }

    // POST /api/jobs/1/stripe-link
    // Returns a Stripe payment URL to send to the customer
    @PostMapping
    public ResponseEntity<?> createPaymentLink(@PathVariable Long jobId) {
        try {
            Job job = jobService.getJobById(jobId);

            if (job.getAgreedPrice() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Job has no agreed price set"));
            }

            String customerEmail = job.getCustomer() != null ? job.getCustomer().getEmail() : null;
            String url = stripeService.createPaymentLink(job, customerEmail);

            return ResponseEntity.ok(Map.of(
                    "url", url,
                    "contractNumber", job.getContractNumber(),
                    "amount", job.getAgreedPrice()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to create payment link: " + e.getMessage()));
        }
    }
}