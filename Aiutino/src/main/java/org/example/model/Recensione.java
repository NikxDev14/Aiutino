package org.example.model;

import java.util.List;

public class Recensione {
    private String utente;
    private String categoria;
    private int stelle;
    private String commento;
    private List<String> frasiIroniche;

    public Recensione(String utente, String categoria, int stelle, String commento, List<String> frasiIroniche) {
        this.utente = utente;
        this.categoria = categoria;
        this.stelle = stelle;
        this.commento = commento;
        this.frasiIroniche = frasiIroniche;
    }

    //Metodo per trasformare la lista di frasi in una stringa unica per il CSV
    public String getFrasiPerCSV() {
        return String.join("|", frasiIroniche);
    }
}
