package com.genius.forum.model;

import jakarta.persistence.*;


import lombok.*;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Community community;

    public Favori(User user, Community community) {
        this.user = user;
        this.community = community;
    }
}