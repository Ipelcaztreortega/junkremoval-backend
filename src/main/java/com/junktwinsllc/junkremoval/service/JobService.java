package com.junktwinsllc.junkremoval.service;

import com.junktwinsllc.junkremoval.model.Customer;
import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CustomerService customerService;

    public JobService(JobRepository jobRepository, CustomerService customerService) {
        this.jobRepository = jobRepository;
        this.customerService = customerService;
    }

    public Job createJob(Long customerId, Job job) {
        Customer customer = customerService.getCustomerById(customerId);
        job.setCustomer(customer);
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));
    }

    public Job getByContractNumber(String contractNumber) {
        return jobRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new RuntimeException("Job not found: " + contractNumber));
    }

    public List<Job> getJobsByCustomer(Long customerId) {
        return jobRepository.findByCustomerId(customerId);
    }

    public List<Job> getJobsByStatus(String status) {
        return jobRepository.findByStatus(status.toUpperCase());
    }

    public Job updateStatus(Long id, String newStatus) {
        Job job = getJobById(id);
        List<String> valid = List.of("QUOTED", "CONFIRMED", "COMPLETED", "PAID", "CANCELLED");
        if (!valid.contains(newStatus.toUpperCase())) {
            throw new RuntimeException("Invalid status: " + newStatus);
        }
        job.setStatus(newStatus.toUpperCase());
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job updated) {
        Job existing = getJobById(id);
        if (updated.getVolume() != null)        existing.setVolume(updated.getVolume());
        if (updated.getDifficulty() != null)    existing.setDifficulty(updated.getDifficulty());
        if (updated.getDistanceMiles() != null) existing.setDistanceMiles(updated.getDistanceMiles());
        if (updated.getAgreedPrice() != null)   existing.setAgreedPrice(updated.getAgreedPrice());
        if (updated.getScheduledAt() != null)   existing.setScheduledAt(updated.getScheduledAt());
        return jobRepository.save(existing);
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found: " + id);
        }
        jobRepository.deleteById(id);
    }
}