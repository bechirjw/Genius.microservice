package com.genuis.ressources;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Session createCheckoutSession(String resourceName, long amount, String successUrl, String cancelUrl) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = List.of(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(amount) // en centimes
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(resourceName)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .addAllLineItem(lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .build();

        return Session.create(params);
    }

    public List<Ressource> getRessourcesAcheteesParUtilisateur(Long userId) {
       // Utilisateur utilisateur = utilisateurRepository.findById(userId).orElseThrow();
       // return utilisateur.getRessourcesAchetees(); // ou une autre logique liée aux paiements
    return (null);//provesoire
    }

}

