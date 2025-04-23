package com.genius.quizservice.dto;
import lombok.Data;

import java.util.List;


@Data

public class SubmitTestDTO {
    private long testId;

    private List<QuestionResponse> responses;
}
