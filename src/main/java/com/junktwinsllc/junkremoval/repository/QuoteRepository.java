package com.junktwinsllc.junkremoval.repository;

import com.junktwinsllc.junkremoval.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Optional<Quote> findByJobId(Long jobId);
    boolean existsByJobId(Long jobId);
}