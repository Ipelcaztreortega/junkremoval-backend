package com.junktwinsllc.junkremoval.service;

import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.model.JobItem;
import com.junktwinsllc.junkremoval.model.Quote;
import com.junktwinsllc.junkremoval.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final JobService jobService;
    private final JobItemService jobItemService;

    // Pricing rates — easy to adjust as your business changes
    private static final BigDecimal RATE_PER_MILE = new BigDecimal("1.50");
    private static final BigDecimal VOLUME_QUARTER = new BigDecimal("75.00");
    private static final BigDecimal VOLUME_HALF    = new BigDecimal("150.00");
    private static final BigDecimal VOLUME_FULL    = new BigDecimal("275.00");
    private static final BigDecimal DIFFICULTY_STAIRS = new BigDecimal("25.00");
    private static final BigDecimal DIFFICULTY_LADDER = new BigDecimal("50.00");
    private static final BigDecimal DIFFICULTY_DEMO   = new BigDecimal("100.00");

    public QuoteService(QuoteRepository quoteRepository, JobService jobService, JobItemService jobItemService) {
        this.quoteRepository = quoteRepository;
        this.jobService = jobService;
        this.jobItemService = jobItemService;
    }

    // Generates a quote automatically from the job's existing data and items
    public Quote generateQuote(Long jobId, BigDecimal weightPrice, BigDecimal dumpFee) {
        Job job = jobService.getJobById(jobId);

        if (quoteRepository.existsByJobId(jobId)) {
            throw new RuntimeException("A quote already exists for job: " + jobId + ". Use update instead.");
        }

        Quote quote = new Quote();
        quote.setJob(job);
        quote.setVolumePrice(calculateVolumePrice(job.getVolume()));
        quote.setItemPrice(calculateItemTotal(jobId));
        quote.setDistanceFee(calculateDistanceFee(job.getDistanceMiles()));
        quote.setDifficultyFee(calculateDifficultyFee(job.getDifficulty()));
        quote.setWeightPrice(weightPrice != null ? weightPrice : BigDecimal.ZERO);
        quote.setDumpFee(dumpFee != null ? dumpFee : BigDecimal.ZERO);

        return quoteRepository.save(quote);
    }

    public Quote getQuoteByJobId(Long jobId) {
        return quoteRepository.findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("No quote found for job: " + jobId));
    }

    // Recalculate and update an existing quote
    public Quote updateQuote(Long jobId, BigDecimal weightPrice, BigDecimal dumpFee) {
        Quote existing = getQuoteByJobId(jobId);
        Job job = jobService.getJobById(jobId);

        existing.setVolumePrice(calculateVolumePrice(job.getVolume()));
        existing.setItemPrice(calculateItemTotal(jobId));
        existing.setDistanceFee(calculateDistanceFee(job.getDistanceMiles()));
        existing.setDifficultyFee(calculateDifficultyFee(job.getDifficulty()));
        if (weightPrice != null) existing.setWeightPrice(weightPrice);
        if (dumpFee != null)     existing.setDumpFee(dumpFee);

        return quoteRepository.save(existing);
    }

    public void deleteQuote(Long jobId) {
        Quote quote = getQuoteByJobId(jobId);
        quoteRepository.delete(quote);
    }

    // --- Pricing helpers ---

    private BigDecimal calculateVolumePrice(String volume) {
        if (volume == null) return BigDecimal.ZERO;
        return switch (volume.toUpperCase()) {
            case "QUARTER" -> VOLUME_QUARTER;
            case "HALF"    -> VOLUME_HALF;
            case "FULL"    -> VOLUME_FULL;
            default        -> BigDecimal.ZERO;
        };
    }

    private BigDecimal calculateItemTotal(Long jobId) {
        List<JobItem> items = jobItemService.getItemsByJob(jobId);
        return items.stream()
                .map(JobItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDistanceFee(BigDecimal distanceMiles) {
        if (distanceMiles == null) return BigDecimal.ZERO;
        return distanceMiles.multiply(RATE_PER_MILE);
    }

    private BigDecimal calculateDifficultyFee(String difficulty) {
        if (difficulty == null) return BigDecimal.ZERO;
        return switch (difficulty.toUpperCase()) {
            case "STAIRS" -> DIFFICULTY_STAIRS;
            case "LADDER" -> DIFFICULTY_LADDER;
            case "DEMO"   -> DIFFICULTY_DEMO;
            default       -> BigDecimal.ZERO;
        };
    }
}