package com.genius.quizservice.entity;

import com.genius.quizservice.dto.TestResultDTO;
import com.genius.quizservice.dto.UserDTO;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    @Column(name = "user_id")
    private Long userId; // ✅ just a primitive field, no relation

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    public TestResultDTO getDto() {
        TestResultDTO dto = new TestResultDTO();

        dto.setId(id);
        dto.setTotalQuestions(totalQuestions);
        dto.setCorrectAnswers(correctAnswers);
        dto.setPercentage(percentage);

        dto.setTestName(test.getTitle());
        dto.setId(userId); // ✅ include if your DTO supports it


        return dto;
    }

}
