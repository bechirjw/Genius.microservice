package tn.esprit.projet4arcticback.user_service.RestController;

import lombok.Data;

@Data
public class FaceMatchResponse {
    private boolean match;
    private double distance;
    private String error;

}
