package org.example.service;

import org.example.model.Recensione;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileRecensioni {

    final static String PATH = "Recensioni.csv";

    public static ArrayList<Recensione> leggiRecensioni() {

        ArrayList<Recensione> recensioni = new ArrayList<>();
        File file = new File(PATH);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
                String riga;
                while ((riga = br.readLine()) != null) {
                    String[] campi = riga.split(";");
                    if (campi.length >= 4) {
                        String utente = campi[0].trim();
                        String categoria = campi[1].trim();
                        int stelle = Integer.parseInt(campi[2].trim());
                        String commento = campi[3].trim();

                        List<String> frasi = new ArrayList<>();
                        if (campi.length >= 5) {
                            //Separa le frasi ironiche salvate con | e le trasforma in una lista
                            String[] frasiArray = campi[4].split("|");
                            frasi = new ArrayList<>(Arrays.asList(frasiArray));
                        }

                        Recensione tmp = new Recensione(utente, categoria, stelle, commento, frasi);
                        recensioni.add(tmp);
                    }
                }
            } catch (Exception e) {
                System.out.println("Errore durante lettura delle recensioni! " + e.getMessage());
            }
        }
        return recensioni;
    }

    public static void salvaRecensione(Recensione r) {

        File file = new File(PATH);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Errore durante la creazione del file recensioni! " + e.getMessage());
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH, true))) {
            bw.write(r.toCsv());
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Errore durante scrittura della recensione! " + e.getMessage());
        }
    }
}