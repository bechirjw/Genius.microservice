package com.genius.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
    private String to;
    private String subject;
    private String body;

}