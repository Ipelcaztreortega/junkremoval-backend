package com.junktwinsllc.junkremoval.repository;

import com.junktwinsllc.junkremoval.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCustomerId(Long customerId);
    Optional<Job> findByContractNumber(String contractNumber);
    List<Job> findByStatus(String status);
    List<Job> findByCustomerIdAndStatus(Long customerId, String status);
}