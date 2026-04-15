package org.example.model;

public class Recensione {
    private String utente, contenuto;
    private Integer numeroStelle;

    public Recensione(String utente, String contenuto, Integer numeroStelle) {
        this.utente = utente;
        this.contenuto = contenuto;
        this.numeroStelle = numeroStelle;
    }
    
}
