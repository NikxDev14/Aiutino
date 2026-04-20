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


    public static void salvaUtente(String email, String password, String username){
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
            Utente tmp = new Utente(username,email,password);
            bw.write(tmp.toCsv());
            bw.newLine();
        }
        catch (Exception e){
            System.out.println("Errore durante scrittura del file! " + e.getMessage());
        }
    }

}