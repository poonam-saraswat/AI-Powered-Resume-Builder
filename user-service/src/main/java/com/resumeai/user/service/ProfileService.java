package com.resumeai.user.service;
import com.resumeai.user.entity.Profile;
import com.resumeai.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repo;
    public List<Profile> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public Profile get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Profile create(Profile e){ return repo.save(e); }
    public Profile update(UUID id, Profile e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
