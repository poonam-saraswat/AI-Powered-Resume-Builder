package com.resumeai.jobmatch.service;
import com.resumeai.jobmatch.entity.Job;
import com.resumeai.jobmatch.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class JobService {
    private final JobRepository repo;
    public List<Job> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public Job get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Job create(Job e){ return repo.save(e); }
    public Job update(UUID id, Job e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
