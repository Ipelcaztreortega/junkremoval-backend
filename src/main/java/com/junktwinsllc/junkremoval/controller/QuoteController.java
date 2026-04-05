package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.QuoteDTO;
import com.junktwinsllc.junkremoval.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/jobs/{jobId}/quote")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<QuoteDTO> generateQuote(
            @PathVariable Long jobId,
            @RequestParam(required = false) BigDecimal weightPrice,
            @RequestParam(required = false) BigDecimal dumpFee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(QuoteDTO.from(quoteService.generateQuote(jobId, weightPrice, dumpFee)));
    }

    @GetMapping
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable Long jobId) {
        return ResponseEntity.ok(QuoteDTO.from(quoteService.getQuoteByJobId(jobId)));
    }

    @PutMapping
    public ResponseEntity<QuoteDTO> updateQuote(
            @PathVariable Long jobId,
            @RequestParam(required = false) BigDecimal weightPrice,
            @RequestParam(required = false) BigDecimal dumpFee) {
        return ResponseEntity.ok(QuoteDTO.from(quoteService.updateQuote(jobId, weightPrice, dumpFee)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteQuote(@PathVariable Long jobId) {
        quoteService.deleteQuote(jobId);
        return ResponseEntity.noContent().build();
    }
}