package com.resumeai.notification.service;
import com.resumeai.notification.entity.Notification;
import com.resumeai.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repo;
    public List<Notification> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public Notification get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Notification create(Notification e){ return repo.save(e); }
    public Notification update(UUID id, Notification e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
