package com.resumeai.jobmatch.repository;
import com.resumeai.jobmatch.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.UUID;
public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByUserId(UUID userId);
}
