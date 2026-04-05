package com.junktwinsllc.junkremoval.repository;

import com.junktwinsllc.junkremoval.model.JobItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobItemRepository extends JpaRepository<JobItem, Long> {
    List<JobItem> findByJobId(Long jobId);
    void deleteByJobId(Long jobId);
}