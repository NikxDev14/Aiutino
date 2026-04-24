package org.example.service;

import org.example.model.Utente;

import javax.swing.*;
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
            boolean emailTrovata = false;
            for (Utente u : utenti){
                if (!email.toLowerCase().equals(u.getEmail())){
                   continue;
                }
                else {
                    emailTrovata = true;
                }
                if (!password.equals(u.getPassword())) {
                    return 3;
                }
                if (!emailTrovata){
                    return 2;
                }
            }
            for (Utente u : utenti){
                if (email.equals(u.getEmail())){
                    tmp = new Utente(u.getUsername(), email, password);
                    break;
                }
            }

            return 0;
        }
    }
    public Utente fornisciUtente(){
        return tmp;
    }

    public static String generaOtp(){
        int numero = (int)(Math.random() * 1000000);
        String otp = String.format("%06d", numero);
        return otp;
    }
    public static boolean verificaOtp(String otp, String otpInserita){
        return otp.equals(otpInserita);
    }

    public void sendMail(String email, String otp) {
        //Caricare file .env
        try{
            EnvLoader.load(".env");
        }catch (Exception e){
            System.out.println("ERRORE LETTURA .ENV");
        }
        //Usiamo funzione di libreria presente in EmailSender
        try{
            EmailSender.send(email, otp);
            System.out.println("Email inviata!");
        }catch (Exception e){
            System.out.println("Errore invio email!");
        }
    }
}
