package org.example.service;

import org.example.model.Utente;

import java.io.*;
import java.util.ArrayList;

public class FileUtenti {

    final static String PATH = "Utenti.csv";
    public static ArrayList<Utente> leggiUtenti(){
        ArrayList<Utente> utenti = new ArrayList<>();
        File file = new File(PATH);


        if (file.exists()){
            try (BufferedReader br = new BufferedReader(new FileReader(PATH))){
                String riga;
                while ((riga = br.readLine()) != null){
                    String[] campi = riga.split(";");
                    if (campi.length >= 3){
                        Utente tmp = new Utente(campi[0].trim(), campi[1].trim(), campi[2].trim());
                        utenti.add(tmp);
                    }
                }
            }
            catch (Exception e){
                System.out.println("Errore durante lettura del file! " + e.getMessage());
            }
            return utenti;
        }
        return utenti;
    }


    public static void salvaUtente(Utente utente){
        File file = new File(PATH);

        if (!file.exists()){
            try {
                file.createNewFile();
            }
            catch (Exception e){
                System.out.println("Errore durante la creazione del file! " + e.getMessage());
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH,true))){
            bw.write(utente.toCsv());
            bw.newLine();
        }
        catch (Exception e){
            System.out.println("Errore durante scrittura del file! " + e.getMessage());
        }
    }

    public static Utente cercaPerEmail(String email) {
        ArrayList<Utente> utenti = leggiUtenti();

        for (Utente u : utenti) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public static String generaUsernameUnico(String nomeUtente) {
        ArrayList<Utente> utenti = leggiUtenti();
        String nuovoUsername = nomeUtente;

        //Genera utente randomico se utente non esiste
        boolean esiste = true;
        while (esiste) {
            esiste = false;
            for (Utente u : utenti) {
                if (u.getUsername().equalsIgnoreCase(nuovoUsername)) {
                    esiste = true;
                    break;
                }
            }
            if (esiste) {
                int tag = (int) (Math.random() * 9000) + 1000;
                nuovoUsername = nomeUtente + "#" + tag;
            }
        }
        return nuovoUsername;
    }

}