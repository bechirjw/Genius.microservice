package com.genius.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean seen = false;   // for unread/read status

    private Long receiverId;  // the user who should receive this notification
    private Long projetId;    // optional: to link to a project
    private Long senderId;


    private String type; // ex: "NEW_COLLABORATION"
    private LocalDateTime timestamp = LocalDateTime.now();
}
