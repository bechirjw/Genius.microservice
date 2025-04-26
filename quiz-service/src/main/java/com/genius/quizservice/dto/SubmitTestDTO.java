package com.genius.quizservice.dto;
import lombok.Data;

import java.util.List;


@Data

public class SubmitTestDTO {
    private long testId;
    private Long userId; // ✅ NEW: passed from frontend
    private List<QuestionResponse> responses;
}
