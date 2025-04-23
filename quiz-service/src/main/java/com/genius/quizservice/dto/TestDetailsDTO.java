package com.genius.quizservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestDetailsDTO {
    private testDTO testDTO;
    private List<QuestionDTO> questions;
}

