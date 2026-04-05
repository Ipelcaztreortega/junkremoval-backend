package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.JobItemDTO;
import com.junktwinsllc.junkremoval.model.JobItem;
import com.junktwinsllc.junkremoval.service.JobItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs/{jobId}/items")
public class JobItemController {

    private final JobItemService jobItemService;

    public JobItemController(JobItemService jobItemService) {
        this.jobItemService = jobItemService;
    }

    @PostMapping
    public ResponseEntity<JobItemDTO> addItem(@PathVariable Long jobId, @RequestBody JobItem item) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JobItemDTO.from(jobItemService.addItem(jobId, item)));
    }

    @GetMapping
    public ResponseEntity<List<JobItemDTO>> getItems(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobItemService.getItemsByJob(jobId).stream()
                .map(JobItemDTO::from).collect(Collectors.toList()));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<JobItemDTO> updateItem(@PathVariable Long jobId, @PathVariable Long itemId, @RequestBody JobItem item) {
        return ResponseEntity.ok(JobItemDTO.from(jobItemService.updateItem(itemId, item)));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long jobId, @PathVariable Long itemId) {
        jobItemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}