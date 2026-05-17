package com.resumeai.resume.service;
import com.resumeai.resume.entity.Resume;
import com.resumeai.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository repo;
    public List<Resume> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public Resume get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Resume create(Resume e){ return repo.save(e); }
    public Resume update(UUID id, Resume e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
