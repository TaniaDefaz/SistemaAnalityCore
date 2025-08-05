package com.analitycore.analyzer.repository;

import com.analitycore.analyzer.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}
