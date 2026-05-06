package org.example.service;

import org.example.model.Utente;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Auth {

    private ArrayList<Utente> utenti = FileUtenti.leggiUtenti();
    Utente tmp = null;  //Per restituire utente in caso di codice 0

    public int controlloErrori(String username, String email, String password, boolean registrazione){
        if (registrazione){
            if (email.isEmpty() || username.isEmpty() || password.isEmpty()){
                return 3;

            }
            for (Utente u : utenti){
                if (username.equals(u.getUsername())){
                    return 1;
                } else if (email.equals(u.getEmail())) {
                    return 2;
                }
            }
            if (!email.contains("@")){
                return 4;
            }
            if (password.length() < 8){
                return 5;
            }
            tmp = new Utente(username, email, password);
            return 0;
        }
        else {  //Se non siamo in fase di registrazione, passa agli errori possibili durante il login
            utenti = FileUtenti.leggiUtenti(); //Ricarica l'array per salvare i nuovi utenti all'interno
            if (email.isEmpty() || password.isEmpty()){
                return 1;
            }
            if (!email.contains("@")){
                return 4;
            }

            Utente utenteTrovato = null;
            for (Utente u : utenti) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    utenteTrovato = u;
                    break;
                }
            }
            if (utenteTrovato == null) {
                return 2;
            }

            if (!utenteTrovato.getPassword().equals(password)) {
                return 3;
            }

            tmp = utenteTrovato;
            return 0;
        }
    }
    public Utente fornisciUtente(){
        return tmp;
    }

    public static String generaOtp(){
        int numero = (int)(Math.random() * 1000000);
        String otp = String.format("%06d", numero); //Da un formato al numero (6 cifre in decimale)
        return otp;
    }
    public static boolean verificaOtp(String otp, String otpInserita){
        return otp.equals(otpInserita);
    }

    public void sendMail(String email, String otp) {
        //Caricare file .env
        try{
            EnvLoader.load();
        }catch (Exception e){
            System.out.println("ERRORE LETTURA .ENV");
        }
        //Usiamo funzione di libreria presente in EmailSender
        try{
            EmailSender.send(email, otp, true);
            System.out.println("Email inviata!");
        }catch (Exception e){
            System.out.println("Errore invio email!");
        }
    }

    public void sendMailAutenticazione(String email) {
        String messaggio = "Caro " + Sessione.getUtente().getUsername() + ", è stato eseguito un nuovo accesso al tuo account su Aiutino alle ore: " + LocalTime.now() + " del giorno " + LocalDate.now();
        //Caricare file .env
        try{
            EnvLoader.load();
        }catch (Exception e){
            System.out.println("ERRORE LETTURA .ENV");
        }
        //Usiamo funzione di libreria presente in EmailSender
        try{
            EmailSender.send(email, messaggio, false);
            System.out.println("Email inviata!");
        }catch (Exception e){
            System.out.println("Errore invio email!");
        }
    }
}
