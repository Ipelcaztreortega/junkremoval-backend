package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.JobDTO;
import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestParam Long customerId, @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JobDTO.from(jobService.createJob(customerId, job)));
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getJobs(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String status) {
        List<Job> results;
        if (customerId != null) results = jobService.getJobsByCustomer(customerId);
        else if (status != null) results = jobService.getJobsByStatus(status);
        else results = jobService.getAllJobs();
        return ResponseEntity.ok(results.stream().map(JobDTO::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(JobDTO.from(jobService.getJobById(id)));
    }

    @GetMapping("/contract/{contractNumber}")
    public ResponseEntity<JobDTO> getByContract(@PathVariable String contractNumber) {
        return ResponseEntity.ok(JobDTO.from(jobService.getByContractNumber(contractNumber)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return ResponseEntity.ok(JobDTO.from(jobService.updateJob(id, job)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(JobDTO.from(jobService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}