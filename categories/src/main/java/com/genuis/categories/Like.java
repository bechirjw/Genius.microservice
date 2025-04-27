package com.genuis.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ressource_id"}))
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ressource_id")
    private Long ressourceId;
}
