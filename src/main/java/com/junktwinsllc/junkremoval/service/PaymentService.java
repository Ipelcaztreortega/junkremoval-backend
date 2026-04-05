package com.junktwinsllc.junkremoval.service;

import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.model.Payment;
import com.junktwinsllc.junkremoval.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JobService jobService;

    public PaymentService(PaymentRepository paymentRepository, JobService jobService) {
        this.paymentRepository = paymentRepository;
        this.jobService = jobService;
    }

    public Payment createPayment(Long jobId, Payment payment) {
        if (paymentRepository.existsByJobId(jobId)) {
            throw new RuntimeException("A payment already exists for job: " + jobId);
        }
        Job job = jobService.getJobById(jobId);
        payment.setJob(job);
        return paymentRepository.save(payment);
    }

    public Payment getPaymentByJobId(Long jobId) {
        return paymentRepository.findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("No payment found for job: " + jobId));
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status.toUpperCase());
    }

    // Mark a payment as PAID and stamp the timestamp
    public Payment markAsPaid(Long jobId, String paymentMethod) {
        Payment payment = getPaymentByJobId(jobId);
        payment.setStatus("PAID");
        payment.setPaymentMethod(paymentMethod.toUpperCase());
        payment.setPaidAt(LocalDateTime.now());

        // Also move the job status to PAID
        jobService.updateStatus(jobId, "PAID");

        return paymentRepository.save(payment);
    }

    public Payment markAsRefunded(Long jobId) {
        Payment payment = getPaymentByJobId(jobId);
        payment.setStatus("REFUNDED");
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long jobId) {
        Payment payment = getPaymentByJobId(jobId);
        paymentRepository.delete(payment);
    }
}