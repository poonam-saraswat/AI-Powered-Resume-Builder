package com.resumeai.export.repository;
import com.resumeai.export.entity.ExportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.UUID;
public interface ExportJobRepository extends JpaRepository<ExportJob, UUID> {
    List<ExportJob> findByUserId(UUID userId);
}
