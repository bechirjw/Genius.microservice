package com.genius.quizservice.entity;

import com.genius.quizservice.dto.TestResultDTO;
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

    private String fullName; // ✅ Full Name
    private String email;    // ✅ Email ajouté ici

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
        dto.setFullName(fullName); // ✅ Envoie aussi le fullName
        // (facultatif) dto.setEmail(email); // si tu ajoutes l'email dans le TestResultDTO

        return dto;
    }
}
