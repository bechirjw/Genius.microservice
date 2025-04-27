package com.genuis.ressources;


public class PaymentRequest {
    private Long utilisateurId;
    private Long ressourceId;
    private String resourceName;
    private long amount;
    private String successUrl;
    private String cancelUrl;
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public Long getUtilisateurId() { return utilisateurId;}
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId;}
    public Long getRessourceId() { return ressourceId;}
    public void setRessourceId(Long ressourceId) { this.ressourceId = ressourceId;}
    // Getters et setters
    
}
