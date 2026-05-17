package com.resumeai.billing.service;
import com.resumeai.billing.entity.Subscription;
import com.resumeai.billing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repo;
    public List<Subscription> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public Subscription get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Subscription create(Subscription e){ return repo.save(e); }
    public Subscription update(UUID id, Subscription e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
