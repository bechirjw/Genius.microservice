package tn.esprit.projet4arcticback.user_service.RestController;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet4arcticback.user_service.service.AuthentificationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthentificationController {
    private final AuthentificationService service;

    @PostMapping("/Register")
    public ResponseEntity<AuthentificationResponse> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        AuthentificationResponse response = service.register(request); // 👈 your service should return this
        return ResponseEntity.ok(response);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthentificationResponse> authenticate(
            @RequestBody @Valid AuthentificationRequest request){

        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }


}