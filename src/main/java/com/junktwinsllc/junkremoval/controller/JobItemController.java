package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.model.JobItem;
import com.junktwinsllc.junkremoval.service.JobItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/items")
public class JobItemController {

    private final JobItemService jobItemService;

    public JobItemController(JobItemService jobItemService) {
        this.jobItemService = jobItemService;
    }

    // POST /api/jobs/1/items
    @PostMapping
    public ResponseEntity<JobItem> addItem(@PathVariable Long jobId, @RequestBody JobItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobItemService.addItem(jobId, item));
    }

    // GET /api/jobs/1/items
    @GetMapping
    public ResponseEntity<List<JobItem>> getItems(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobItemService.getItemsByJob(jobId));
    }

    // PUT /api/jobs/1/items/3
    @PutMapping("/{itemId}")
    public ResponseEntity<JobItem> updateItem(@PathVariable Long jobId, @PathVariable Long itemId, @RequestBody JobItem item) {
        return ResponseEntity.ok(jobItemService.updateItem(itemId, item));
    }

    // DELETE /api/jobs/1/items/3
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long jobId, @PathVariable Long itemId) {
        jobItemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}