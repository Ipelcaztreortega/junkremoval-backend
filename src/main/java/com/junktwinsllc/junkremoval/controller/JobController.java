package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // POST /api/jobs?customerId=1
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestParam Long customerId, @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(customerId, job));
    }

    // GET /api/jobs  |  ?customerId=1  |  ?status=QUOTED
    @GetMapping
    public ResponseEntity<List<Job>> getJobs(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String status) {
        if (customerId != null) return ResponseEntity.ok(jobService.getJobsByCustomer(customerId));
        if (status != null)     return ResponseEntity.ok(jobService.getJobsByStatus(status));
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/contract/{contractNumber}")
    public ResponseEntity<Job> getByContract(@PathVariable String contractNumber) {
        return ResponseEntity.ok(jobService.getByContractNumber(contractNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    // PATCH /api/jobs/1/status?status=CONFIRMED
    @PatchMapping("/{id}/status")
    public ResponseEntity<Job> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(jobService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}