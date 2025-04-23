package com.genius.quizservice.entity;

import com.genius.quizservice.dto.QuestionDTO;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;  // Add the import for Jackson

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonIgnore  // Prevent serialization of the associated Test
    private Test test;

    public QuestionDTO getDto() {
        QuestionDTO dto = new QuestionDTO();

        dto.setId(id);
        dto.setQuestionText(questionText);
        dto.setOptionA(optionA);
        dto.setOptionB(optionB);
        dto.setOptionC(optionC);
        dto.setOptionD(optionD);
        dto.setCorrectOption(correctOption);

        return dto;
    }
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                '}';
    }
}
