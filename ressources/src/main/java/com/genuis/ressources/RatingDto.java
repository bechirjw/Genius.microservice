package com.genuis.ressources;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {
    private Long resourceId;
    private String username;
    private Long userId;
    private int rating;
    private String comment;


}
