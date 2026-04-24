package org.example.service;

import java.io.*;
import java.util.*;

public class FileFrasi {

    final static String PATH = "Frasi.csv";

    public static Map<String, List<String>> caricaFrasiPerCategoria() {

        Map<String, List<String>> mappa = new HashMap<>();
        File file = new File(PATH);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
                String riga;
                while ((riga = br.readLine()) != null) {
                    String[] campi = riga.split(";");
                    if (campi.length >= 2) {
                        String categoria = campi[0].trim();
                        String frase = campi[1].trim();

                        //Se legge categoria non esistente la crea
                        mappa.computeIfAbsent(categoria, k -> new ArrayList<>()).add(frase);
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore caricamento frasi: " + e.getMessage());
            }
        }
        return mappa;
    }
}