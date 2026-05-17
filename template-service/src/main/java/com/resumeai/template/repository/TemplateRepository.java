package com.resumeai.template.repository;
import com.resumeai.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
    List<Template> findByUserId(UUID userId);
    List<Template> findByIsSampleTrue();
}
