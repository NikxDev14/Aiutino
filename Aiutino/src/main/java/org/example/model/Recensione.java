package org.example.model;

import java.util.List;

public class Recensione {
    private String utente;
    private String categoria;
    private int stelle;
    private String indirizzo;
    private String commento;
    private List<String> frasiIroniche;

    public Recensione(String utente, String categoria, int stelle,String indirizzo, String commento, List<String> frasiIroniche) {
        this.utente = utente;
        this.categoria = categoria;
        this.stelle = stelle;
        this.indirizzo = indirizzo;
        this.commento = commento;
        this.frasiIroniche = frasiIroniche;
    }

    public String getUtente() {
        return utente;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getStelle() {
        return stelle;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getCommento() {
        return commento;
    }

    public List<String> getFrasiIroniche() {
        return frasiIroniche;
    }

    public String toCsv(){
        String commento = this.commento != null ? this.commento.replace(";",",") : ""; //Se il commento non è vuoto sostituisce ; con ,
        String frasi = String.join("|", frasiIroniche); //Unisce le frasi ironiche spuntate separandole con |
        return utente + ";" + categoria + ";" + stelle + ";" + indirizzo +";"+ commento + ";" + frasi;
    }
}
