package com.resumeai.export.service;
import com.resumeai.export.entity.ExportJob;
import com.resumeai.export.repository.ExportJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class ExportJobService {
    private final ExportJobRepository repo;
    public List<ExportJob> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public ExportJob get(UUID id){ return repo.findById(id).orElseThrow(); }
    public ExportJob create(ExportJob e){ return repo.save(e); }
    public ExportJob update(UUID id, ExportJob e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
