package com.genuis.ressources;

import com.google.gson.JsonObject;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.checkout.Session; // ✅ Correct pour Stripe Checkout
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.stripe.model.checkout.Session;
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private StripeService stripeService;
    @Autowired
    private AchatService achatService;
    @Value("${stripe.webhook.secret.key}")
    private String webhookSecret;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequest request) {
        try {
            Session session = stripeService.createCheckoutSession(
                    request.getUtilisateurId(),
                    request.getRessourceId(),
                    request.getResourceName(),
                    request.getAmount(),
                    request.getSuccessUrl(),
                    request.getCancelUrl()

            );
            Map<String, String> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    private final String endpointSecret = "whsec_595d0e5ec5a7ff2da8c705a9edd4ac10dfdb28d7d0efefbbcd39d32fc1cd296d"; // Remplace par ton secret de webhook

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);

                JsonObject sessionObject = jsonObject.getAsJsonObject("data").getAsJsonObject("object");

                String clientReferenceId = sessionObject.get("client_reference_id").getAsString();
                String ressourceIdStr = sessionObject.getAsJsonObject("metadata").get("ressourceId").getAsString();

                System.out.println("🧩 clientReferenceId = " + clientReferenceId);
                System.out.println("🧩 ressourceIdStr = " + ressourceIdStr);

                Long utilisateurId = Long.parseLong(clientReferenceId);
                Long ressourceId = Long.parseLong(ressourceIdStr);

                achatService.enregistrerAchat(utilisateurId, ressourceId);


                return ResponseEntity.ok("Achat enregistré avec succès");

            }

            return ResponseEntity.ok("Événement ignoré");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature Stripe invalide");
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne : " + e.getMessage());
        }
    }

    @GetMapping("/user/{utilisateurId}/ressources")
    public ResponseEntity<List<Ressource>> getRessourcesAcheteesParUtilisateur(@PathVariable Long utilisateurId) {
        List<Ressource> ressources = achatService.getRessourcesAcheteesParUtilisateur(utilisateurId);
        return ResponseEntity.ok(ressources);
    }

}

