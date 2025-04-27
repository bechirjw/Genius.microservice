package com.genius.notification;

import com.genius.notification.Notification;
import com.genius.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByReceiverId(userId);
    }

    public void markAsSeen(Long notifId) {
        Notification notif = notificationRepository.findById(notifId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setSeen(true);
        notificationRepository.save(notif);
    }
}
