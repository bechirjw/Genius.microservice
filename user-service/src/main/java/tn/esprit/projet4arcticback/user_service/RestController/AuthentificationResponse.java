package tn.esprit.projet4arcticback.user_service.RestController;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthentificationResponse {


    private String token;
   private long id;
   private  String email;
    public AuthentificationResponse(String token, Long id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }
}
