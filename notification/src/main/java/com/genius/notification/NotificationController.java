package com.genius.notification;

import com.genius.notification.Notification;
import com.genius.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public Notification sendNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotifications(@PathVariable("userId") Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PutMapping("/seen/{notifId}")
    public void markAsSeen(@PathVariable("notifId") Long notifId) {
        notificationService.markAsSeen(notifId);
    }



}
