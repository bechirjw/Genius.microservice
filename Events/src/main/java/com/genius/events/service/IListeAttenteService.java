package com.genius.events.service;


import com.genius.events.entity.Evenements;
import com.genius.events.entity.ListeAttente;

public interface IListeAttenteService {
    ListeAttente inscrire(ListeAttente demande);
    void notifierPremierEnAttente(Evenements evenement);

}
