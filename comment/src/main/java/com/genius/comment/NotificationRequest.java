package com.genius.comment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String message;
    private Long receiverId;
    private Long senderId;
    private String type;
    private Long postId;
}
