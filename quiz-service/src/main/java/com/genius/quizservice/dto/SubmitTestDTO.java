package com.genius.quizservice.dto;
import lombok.Data;

import java.util.List;


@Data

public class SubmitTestDTO {
    private long testId;
    private String email;   // ✅ ➔ Ajout de l'email
    private String fullName; // ✅ Ajouté le champ fullName
    private List<QuestionResponse> responses;
}
