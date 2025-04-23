package com.genius.quizservice.entity;

import com.genius.quizservice.dto.testDTO;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;  // Add the import for Jackson

import java.util.List;

@Entity
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long time;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Prevent serialization of questions
    private List<Question> questions;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Prevent serialization of testResults
    private List<TestResult> testResults;

    public testDTO getDto() {
        testDTO testDTO = new testDTO();

        testDTO.setId(id);
        testDTO.setTitle(title);
        testDTO.setDescription(description);
        testDTO.setTime(time);

        return testDTO;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                // Don't include questions in the toString to avoid recursion
                '}';
    }
}

