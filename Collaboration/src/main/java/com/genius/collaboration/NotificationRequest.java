package com.genius.collaboration;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private Long receiverId;
    private String type;
    private Long projetId;
}
