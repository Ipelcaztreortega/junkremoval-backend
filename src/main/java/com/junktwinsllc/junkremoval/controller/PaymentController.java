package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.PaymentDTO;
import com.junktwinsllc.junkremoval.model.Payment;
import com.junktwinsllc.junkremoval.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs/{jobId}/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // POST /api/jobs/1/payment
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable Long jobId, @RequestBody Payment payment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentDTO.from(paymentService.createPayment(jobId, payment)));
    }

    // GET /api/jobs/1/payment
    @GetMapping
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long jobId) {
        return ResponseEntity.ok(PaymentDTO.from(paymentService.getPaymentByJobId(jobId)));
    }

    // GET /api/payments?status=PENDING  (note: different base path)
    // See PaymentStatusController below or add to a separate controller if needed

    // PATCH /api/jobs/1/payment/pay?method=CASH
    @PatchMapping("/pay")
    public ResponseEntity<PaymentDTO> markAsPaid(@PathVariable Long jobId, @RequestParam String method) {
        return ResponseEntity.ok(PaymentDTO.from(paymentService.markAsPaid(jobId, method)));
    }

    // PATCH /api/jobs/1/payment/refund
    @PatchMapping("/refund")
    public ResponseEntity<PaymentDTO> markAsRefunded(@PathVariable Long jobId) {
        return ResponseEntity.ok(PaymentDTO.from(paymentService.markAsRefunded(jobId)));
    }

    // DELETE /api/jobs/1/payment
    @DeleteMapping
    public ResponseEntity<Void> deletePayment(@PathVariable Long jobId) {
        paymentService.deletePayment(jobId);
        return ResponseEntity.noContent().build();
    }
}