package com.genuis.ressources;

import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.checkout.Session; // ✅ Correct pour Stripe Checkout

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequest request) {
        try {
            Session session = stripeService.createCheckoutSession(
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

    @GetMapping("/user/{userId}/ressources")
    public List<Ressource> getRessourcesAcheteesParUtilisateur(@PathVariable Long userId) {
        return StripeService.getRessourcesAcheteesParUtilisateur(userId);
    }
}

