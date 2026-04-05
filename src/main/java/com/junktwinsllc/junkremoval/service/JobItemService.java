package com.junktwinsllc.junkremoval.service;

import com.junktwinsllc.junkremoval.model.Job;
import com.junktwinsllc.junkremoval.model.JobItem;
import com.junktwinsllc.junkremoval.repository.JobItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobItemService {

    private final JobItemRepository jobItemRepository;
    private final JobService jobService;

    public JobItemService(JobItemRepository jobItemRepository, JobService jobService) {
        this.jobItemRepository = jobItemRepository;
        this.jobService = jobService;
    }

    public JobItem addItem(Long jobId, JobItem item) {
        Job job = jobService.getJobById(jobId);
        item.setJob(job);
        return jobItemRepository.save(item);
    }

    public List<JobItem> getItemsByJob(Long jobId) {
        return jobItemRepository.findByJobId(jobId);
    }

    public JobItem updateItem(Long itemId, JobItem updated) {
        JobItem existing = jobItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));
        if (updated.getItemName() != null)  existing.setItemName(updated.getItemName());
        if (updated.getQuantity() != null)  existing.setQuantity(updated.getQuantity());
        if (updated.getUnitPrice() != null) existing.setUnitPrice(updated.getUnitPrice());
        return jobItemRepository.save(existing);
    }

    public void deleteItem(Long itemId) {
        if (!jobItemRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found: " + itemId);
        }
        jobItemRepository.deleteById(itemId);
    }

    public void deleteAllItemsForJob(Long jobId) {
        jobItemRepository.deleteByJobId(jobId);
    }
}