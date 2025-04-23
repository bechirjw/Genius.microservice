package com.genius.quizservice.dto;
import lombok.Data;



@Data

public class QuestionResponse {

    private long questionId;

    private String selectedOption;
}
