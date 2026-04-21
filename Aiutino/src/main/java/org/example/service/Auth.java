package org.example.service;

import org.example.model.Utente;

import java.util.ArrayList;

public class Auth {

    private ArrayList<Utente> utenti = FileUtenti.leggiUtenti();

    public int registrati(String username, String email, String password){

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
        FileUtenti.salvaUtente(email,password,username);
        return 0;
    }

    public static String generaOtp(){
        int numero = (int)(Math.random() * 1000000);
        String otp = String.format("%06d", numero);
        return otp;
    }
}
